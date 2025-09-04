package project.bookstore.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
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
import project.bookstore.dto.book.BookDtoWithoutCategoryIds;
import project.bookstore.dto.category.CategoryDto;
import project.bookstore.dto.category.CreateCategoryRequestDto;
import project.bookstore.dto.category.UpdateCategoryRequestDto;
import project.bookstore.util.ControllerRepositoryTestUtil;

@Sql(scripts = {
        "classpath:database/booksCategories/clear-books-categories-table.sql",
        "classpath:database/category/clear-categories-table.sql",
        "classpath:database/category/add-two-default-categories-to-categories-table.sql"
},
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CategoryControllerTest {
    protected static MockMvc mockMvc;
    private static BookDtoWithoutCategoryIds gatsbyWithoutCategoryIdsExpected;
    private static CreateCategoryRequestDto createTestCategoryRequest;
    private static CategoryDto testCategoryDtoExpected;
    private static CategoryDto updatedFictionCategoryDtoExpected;
    private static CategoryDto fictionCategoryDtoExpected;
    private static UpdateCategoryRequestDto emptyUpdateCategoryRequest;
    private static UpdateCategoryRequestDto updateFictionCategoryRequest;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(@Autowired WebApplicationContext applicationContext) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();
        createTestCategoryRequest =
                ControllerRepositoryTestUtil.getCreateTestCategoryRequest();
        testCategoryDtoExpected =
                ControllerRepositoryTestUtil.getTestCategoryDtoExpected();
        fictionCategoryDtoExpected =
                ControllerRepositoryTestUtil.getFictionCategoryDtoExpected();
        updateFictionCategoryRequest =
                ControllerRepositoryTestUtil.getUpdateFictionCategoryRequest();
        updatedFictionCategoryDtoExpected =
                ControllerRepositoryTestUtil.getUpdatedFictionCategoryDtoExpected();
        emptyUpdateCategoryRequest =
                ControllerRepositoryTestUtil.getEmptyUpdateCategoryRequest();
        gatsbyWithoutCategoryIdsExpected =
                ControllerRepositoryTestUtil.getGatsbyWithoutCategoryIdsExpected();
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void createCategory_validRequest_returnsCategoryDto() throws Exception {
        String jsonRequest = objectMapper.writeValueAsString(createTestCategoryRequest);

        MvcResult result = mockMvc.perform(post("/categories")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andReturn();
        CategoryDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                CategoryDto.class
        );

        assertNotNull(actual);
        assertEquals(testCategoryDtoExpected, actual);
    }

    @WithMockUser(username = "user", roles = {"USER"})
    @Test
    void getAll_returnsAllCategories() throws Exception {
        MvcResult result = mockMvc.perform(get("/categories")
                        .param("page", "0")
                        .param("size", "10")
                        .param("sort", "id,asc")
                )
                .andExpect(status().isOk())
                .andReturn();

        JsonNode jsonNode = objectMapper.readTree(result.getResponse().getContentAsString());
        JsonNode content = jsonNode.get("content");
        assertEquals(2, content.size());

        CategoryDto fictionActual = objectMapper.treeToValue(content.get(0), CategoryDto.class);
        assertEquals(fictionCategoryDtoExpected, fictionActual);

        CategoryDto comedyActual = objectMapper.treeToValue(content.get(1), CategoryDto.class);
        assertEquals(fictionCategoryDtoExpected, comedyActual);
    }

    @WithMockUser(username = "user", roles = {"USER"})
    @Test
    void getCategoryById_validId_returnsCategoryDto() throws Exception {
        MvcResult result = mockMvc.perform(get("/categories/{id}", 1L)
                )
                .andExpect(status().isOk())
                .andReturn();

        CategoryDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                CategoryDto.class
        );

        assertNotNull(actual);
        assertEquals(fictionCategoryDtoExpected, actual);
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    void updateCategory_validRequest_returnsUpdatedCategoryDto() throws Exception {
        String jsonRequest = objectMapper.writeValueAsString(updateFictionCategoryRequest);

        MvcResult result = mockMvc.perform(put("/categories/{id}", 1L)
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        CategoryDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                CategoryDto.class
        );
        assertNotNull(actual);
        assertEquals(updatedFictionCategoryDtoExpected, actual);
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    void updateCategory_nullValuesInRequest_returnsUpdatedCategoryDto() throws Exception {
        String jsonRequest = objectMapper.writeValueAsString(emptyUpdateCategoryRequest);

        MvcResult result = mockMvc.perform(put("/categories/{id}", 1L)
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        CategoryDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                CategoryDto.class
        );
        assertNotNull(actual);
        assertEquals(fictionCategoryDtoExpected, actual);
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    void deleteCategory_validId_removesCategoryFromDb() throws Exception {
        mockMvc.perform(delete("/categories/{id}", 1L)
                )
                .andExpect(status().isNoContent())
                .andReturn();

        mockMvc.perform(get("/categories/{id}", 1L))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Sql(scripts = {
            "classpath:database/booksCategories/clear-books-categories-table.sql",
            "classpath:database/book/clear-books-table.sql",
            "classpath:database/book/add-two-default-books-to-books-table.sql",
            "classpath:database/booksCategories/insert-into-books-categories.sql"
    },
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @WithMockUser(username = "user", roles = {"USER"})
    @Test
    void getBooksByCategoryId() throws Exception {
        MvcResult result = mockMvc.perform(
                get("/categories/{id}/books", 1L)
                        .param("page", "0")
                        .param("size", "10")
                        .param("sort", "id,asc")
                )
                .andExpect(status().isOk())
                .andReturn();

        JsonNode jsonNode = objectMapper.readTree(result.getResponse().getContentAsString());
        JsonNode content = jsonNode.get("content");

        BookDtoWithoutCategoryIds gatsbyActualBookDto = objectMapper.treeToValue(
                content.get(0), BookDtoWithoutCategoryIds.class
        );
        assertEquals(gatsbyWithoutCategoryIdsExpected, gatsbyActualBookDto);

        BookDtoWithoutCategoryIds crimeActualBookDto = objectMapper.treeToValue(
                content.get(1), BookDtoWithoutCategoryIds.class
        );
        assertEquals(gatsbyWithoutCategoryIdsExpected, crimeActualBookDto);
    }
}
