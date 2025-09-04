package project.bookstore.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.shaded.org.apache.commons.lang3.builder.EqualsBuilder;
import project.bookstore.dto.book.BookDto;
import project.bookstore.dto.book.BookSearchParameters;
import project.bookstore.dto.book.CreateBookRequestDto;
import project.bookstore.dto.book.UpdateBookRequestDto;
import project.bookstore.repository.BookRepository;
import project.bookstore.util.ControllerRepositoryTestUtil;

@Sql(scripts = {
        "classpath:database/booksCategories/clear-books-categories-table.sql",
        "classpath:database/book/clear-books-table.sql",
        "classpath:database/category/clear-categories-table.sql",
        "classpath:database/category/add-default-category-to-categories-table.sql",
        "classpath:database/book/add-two-default-books-to-books-table.sql",
        "classpath:database/booksCategories/insert-into-books-categories.sql"
},
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BookControllerTest {
    public static final Long ID_FIRST = 1L;
    protected static MockMvc mockMvc;
    private static BookDto crimeBookDtoExpected;
    private static BookDto gatsbyBookDtoExpected;
    private static BookDto potterBookDtoExpected;
    private static BookDto updatedGatsbyBookDtoExpected;
    private static BookSearchParameters gatsbyBookSearchParameters;
    private static CreateBookRequestDto createPotterBookRequest;
    private static UpdateBookRequestDto updateGatsbyBookRequest;

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private BookRepository bookRepository;

    @BeforeAll
    static void beforeAll(@Autowired WebApplicationContext applicationContext) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();
        gatsbyBookDtoExpected = ControllerRepositoryTestUtil.getGatsbyBookDtoExpected();
        crimeBookDtoExpected = ControllerRepositoryTestUtil.getCrimeBookDtoExpected();
        createPotterBookRequest = ControllerRepositoryTestUtil.getCreatePotterBookRequest();
        potterBookDtoExpected = ControllerRepositoryTestUtil.getPotterBookDtoExpected();
        updateGatsbyBookRequest = ControllerRepositoryTestUtil.getUpdateGatsbyBookRequest();
        updatedGatsbyBookDtoExpected =
                ControllerRepositoryTestUtil.getUpdatedGatsbyBookDtoExpected();
        gatsbyBookSearchParameters = ControllerRepositoryTestUtil.getGatsbyBookSearchParameters();
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    void createBook_validRequestDto_returnsBookDto() throws Exception {
        String jsonRequest = objectMapper.writeValueAsString(
                createPotterBookRequest
        );
        MvcResult result = mockMvc.perform(post("/books")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andReturn();

        BookDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                BookDto.class
        );

        assertNotNull(actual);
        assertNotNull(actual.id());
        assertTrue(
                EqualsBuilder.reflectionEquals(
                        potterBookDtoExpected,
                        actual,
                        "id")
        );
    }

    @Test
    @DisplayName("Returns a book when ID is valid")
    @WithMockUser(username = "user", roles = {"USER"})
    void getBookById_validId_returnsBookDto() throws Exception {
        MvcResult result = mockMvc.perform(
                        get("/books/{id}", ID_FIRST)
                )
                .andExpect(status().isOk())
                .andReturn();
        String contentAsString = result.getResponse().getContentAsString();
        BookDto actual = objectMapper.readValue(contentAsString, BookDto.class);

        assertNotNull(actual);
        assertEquals(gatsbyBookDtoExpected, actual);
    }

    @Test
    @DisplayName("Returns all books from DB")
    @WithMockUser(username = "user", roles = {"USER"})
    void getAll_returnsAllBooks() throws Exception {
        MvcResult result = mockMvc.perform(
                        get("/books")
                                .param("page", "0")
                                .param("size", "10")
                                .param("sort", "id,asc")
                )
                .andExpect(status().isOk())
                .andReturn();

        JsonNode jsonNode = objectMapper.readTree(result.getResponse().getContentAsString());
        JsonNode content = jsonNode.get("content");
        assertEquals(2, content.size());

        BookDto gatsbyActual = objectMapper.treeToValue(content.get(0), BookDto.class);
        assertEquals(gatsbyBookDtoExpected, gatsbyActual);

        BookDto crimeActual = objectMapper.treeToValue(content.get(1), BookDto.class);
        assertEquals(crimeBookDtoExpected, crimeActual);
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    void delete_validId_removesBookByIdFromDB() throws Exception {
        mockMvc.perform(delete("/books/{id}", 1L))
                .andExpect(status().isNoContent())
                .andReturn();

        assertFalse(bookRepository.existsById(ID_FIRST));
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    void update_validRequest_updatesBook() throws Exception {
        String jsonRequest = objectMapper.writeValueAsString(updateGatsbyBookRequest);
        MvcResult result = mockMvc.perform(put("/books/{id}", ID_FIRST)
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        BookDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                BookDto.class
        );
        assertEquals(updatedGatsbyBookDtoExpected, actual);
    }

    @DisplayName("Search books with parameters; returns matching books")
    @WithMockUser(username = "user", roles = {"USER"})
    @Test
    void search_validRequest_returnsBookDtos() throws Exception {
        String jsonRequest = objectMapper.writeValueAsString(gatsbyBookSearchParameters);
        MvcResult result = mockMvc.perform(get("/books/search")
                        .param("page", "0")
                        .param("size", "10")
                        .param("sort", "id,asc")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        JsonNode jsonNode = objectMapper.readTree(result.getResponse().getContentAsString());
        JsonNode content = jsonNode.get("content");
        assertEquals(1, content.size());

        BookDto actual = objectMapper.treeToValue(content.get(0), BookDto.class);
        assertEquals(gatsbyBookDtoExpected, actual);
    }
}
