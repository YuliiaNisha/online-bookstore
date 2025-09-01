package project.bookstore.controller;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.Set;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
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

@Sql(scripts = {"classpath:database/booksCategories/clear-books-categories-table.sql",
        "classpath:database/category/clear-categories-table.sql",
        "classpath:database/category/add-default-category-to-categories-table.sql",
        "classpath:database/book/clear-books-table.sql",
        "classpath:database/book/add-two-default-books-to-books-table.sql",
        "classpath:database/booksCategories/insert-into-books-categories.sql"
},
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BookControllerTest {
    public static final long ID_FIRST = 1L;
    protected static MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private BookRepository bookRepository;
    private BookDto bookDto1;
    private BookDto bookDto2;

    @BeforeAll
    static void beforeAll(@Autowired WebApplicationContext applicationContext) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();
    }

    @BeforeEach
    void setUp() {
        bookDto1 = new BookDto(
                1L,
                "The Great Gatsby",
                "F. Scott Fitzgerald",
                "9780743273565",
                BigDecimal.valueOf(15.99),
                "Classic novel set in the 1920s",
                "great_gatsby.jpg",
                Set.of(ID_FIRST)
        );
        bookDto2 = new BookDto(
                2L,
                "1984",
                "George Orwell",
                "9780451524935",
                BigDecimal.valueOf(12.99),
                "Dystopian novel about totalitarianism",
                "1984.jpg",
                Set.of(ID_FIRST)
        );
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    void createBook_validRequestDto_returnsBookDto() throws Exception {
        CreateBookRequestDto requestDto = new CreateBookRequestDto(
                "Harry Potter and the Philosopher's Stone",
                "J. K. Rowling",
                "978-0747532699",
                BigDecimal.valueOf(12.50),
                "The first book in the Harry Potter series, "
                        + "following a young wizard discovering his magical heritage.",
                "https://example.com/harry-potter.jpg",
                Set.of(ID_FIRST)
        );
        String jsonRequest = objectMapper.writeValueAsString(requestDto);
        BookDto expected = new BookDto(
                1L,
                "Harry Potter and the Philosopher's Stone",
                "J. K. Rowling",
                "978-0747532699",
                BigDecimal.valueOf(12.50),
                "The first book in the Harry Potter series, "
                        + "following a young wizard discovering his magical heritage.",
                "https://example.com/harry-potter.jpg",
                Set.of(ID_FIRST)
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

        Assertions.assertNotNull(actual);
        Assertions.assertNotNull(actual.id());
        Assertions.assertTrue(
                EqualsBuilder.reflectionEquals(expected, actual, "id")
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

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(bookDto1, actual);
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
        Assertions.assertEquals(2, content.size());

        BookDto actual1 = objectMapper.treeToValue(content.get(0), BookDto.class);
        Assertions.assertEquals(bookDto1, actual1);

        BookDto actual2 = objectMapper.treeToValue(content.get(1), BookDto.class);
        Assertions.assertEquals(bookDto2, actual2);
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    void delete_validId_removesBookByIdFromDB() throws Exception {
        MvcResult result = mockMvc.perform(delete("/books/{id}", 1L))
                .andExpect(status().isNoContent())
                .andReturn();

        Assertions.assertFalse(bookRepository.existsById(ID_FIRST));
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    void update_validRequest_updatesBook() throws Exception {
        UpdateBookRequestDto updateBookRequestDto = new UpdateBookRequestDto(
                "The Great Gatsby updated",
                null,
                null,
                null,
                null,
                null,
                null
        );
        BookDto expected = new BookDto(
                1L,
                "The Great Gatsby updated",
                "F. Scott Fitzgerald",
                "9780743273565",
                BigDecimal.valueOf(15.99),
                "Classic novel set in the 1920s",
                "great_gatsby.jpg",
                Set.of(ID_FIRST)
        );
        String jsonRequest = objectMapper.writeValueAsString(updateBookRequestDto);
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
        Assertions.assertEquals(expected, actual);
    }

    @DisplayName("Search books with parameters; returns matching books")
    @WithMockUser(username = "user", roles = {"USER"})
    @Test
    void search_validRequest_returnsBookDtos() throws Exception {
        BookSearchParameters bookSearchParameters = new BookSearchParameters(
                "Gatsby",
                null,
                null,
                null
        );

        String jsonRequest = objectMapper.writeValueAsString(bookSearchParameters);

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
        Assertions.assertEquals(1, content.size());

        BookDto actual = objectMapper.treeToValue(content.get(0), BookDto.class);
        Assertions.assertEquals(bookDto1, actual);
    }
}
