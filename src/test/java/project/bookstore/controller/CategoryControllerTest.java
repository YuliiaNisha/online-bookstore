package project.bookstore.controller;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Assertions;
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
import org.testcontainers.shaded.com.fasterxml.jackson.databind.JsonNode;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import org.testcontainers.shaded.org.apache.commons.lang3.builder.EqualsBuilder;
import project.bookstore.dto.book.BookDtoWithoutCategoryIds;
import project.bookstore.dto.category.CategoryDto;
import project.bookstore.dto.category.CreateCategoryRequestDto;
import project.bookstore.dto.category.UpdateCategoryRequestDto;

@Sql(scripts = {
        "classpath:database/category/clear-categories-table.sql"
}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CategoryControllerTest {
    protected static MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    private final CategoryDto expectedCategoryDto1 = getCategoryDto1();
    private final CategoryDto expectedCategoryDto2 = getCategoryDto2();

    @BeforeAll
    static void beforeAll(@Autowired WebApplicationContext applicationContext) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void createCategory_validRequest_returnsCategoryDto() throws Exception {
        CreateCategoryRequestDto requestDto = new CreateCategoryRequestDto(
                "Test Category",
                "Category for testing"
        );
        CategoryDto expected = new CategoryDto(
                1L,
                "Test Category",
                "Category for testing"
        );
        String jsonRequest = objectMapper.writeValueAsString(requestDto);

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

        Assertions.assertNotNull(actual);
        Assertions.assertTrue(
                EqualsBuilder.reflectionEquals(expected, actual, "id")
        );
    }

    @Sql(
            scripts = "classpath:database/category/"
                    + "add-two-default-categories-to-categories-table.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
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
        Assertions.assertEquals(2, content.size());

        CategoryDto actual1 = objectMapper.treeToValue(content.get(0), CategoryDto.class);
        Assertions.assertEquals(expectedCategoryDto1, actual1);

        CategoryDto actual2 = objectMapper.treeToValue(content.get(1), CategoryDto.class);
        Assertions.assertEquals(expectedCategoryDto2, actual2);
    }

    @Sql(scripts = "classpath:database/category/add-two-default-categories-to-categories-table.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
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

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(expectedCategoryDto1, actual);
    }

    @Sql(scripts = "classpath:database/category/add-two-default-categories-to-categories-table.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    void updateCategory_validRequest_returnsUpdatedCategoryDto() throws Exception {
        CategoryDto expected = new CategoryDto(
                1L,
                "Default category1 updated",
                "Default category1 description"
        );
        UpdateCategoryRequestDto requestDto = new UpdateCategoryRequestDto(
                "Default category1 updated",
                null
        );
        String jsonRequest = objectMapper.writeValueAsString(requestDto);

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
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(expected, actual);
    }

    @Sql(scripts = "classpath:database/category/add-two-default-categories-to-categories-table.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    void updateCategory_nullValuesInRequest_returnsUpdatedCategoryDto() throws Exception {
        UpdateCategoryRequestDto requestDto = new UpdateCategoryRequestDto(
                null,
                null
        );
        String jsonRequest = objectMapper.writeValueAsString(requestDto);

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
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(expectedCategoryDto1, actual);
    }

    @Sql(scripts = "classpath:database/category/add-two-default-categories-to-categories-table.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
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
            "classpath:database/category/add-two-default-categories-to-categories-table.sql",
            "classpath:database/booksCategories/add-two-default-books-to-books-table.sql",
            "classpath:database/booksCategories/insert-into-books-categories.sql"
    },
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
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

        BookDtoWithoutCategoryIds actualBookDto1 = objectMapper.treeToValue(
                content.get(0), BookDtoWithoutCategoryIds.class
        );
        Assertions.assertEquals("The Great Gatsby", actualBookDto1.title());

        BookDtoWithoutCategoryIds actualBookDto2 = objectMapper.treeToValue(
                content.get(1), BookDtoWithoutCategoryIds.class
        );
        Assertions.assertEquals("1984", actualBookDto2.title());
    }

    private CategoryDto getCategoryDto1() {
        return new CategoryDto(
                1L,
                "Default category1",
                "Default category1 description"
        );
    }

    private CategoryDto getCategoryDto2() {
        return new CategoryDto(
                2L,
                "Default category2",
                "Default category2 description"
        );
    }
}
