package project.bookstore.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import project.bookstore.dto.book.BookDtoWithoutCategoryIds;
import project.bookstore.dto.category.CategoryDto;
import project.bookstore.dto.category.CreateCategoryRequestDto;
import project.bookstore.dto.category.UpdateCategoryRequestDto;
import project.bookstore.service.book.BookService;
import project.bookstore.service.category.CategoryService;

@Tag(name = "Book Categories",
        description = "Endpoints for managing book categories")
@RequiredArgsConstructor
@RequestMapping("/categories")
@RestController
public class CategoryController {
    private final CategoryService categoryService;
    private final BookService bookService;

    @Operation(summary = "Create a new category",
            description = "Adds a new category to DB",
            responses = {
                    @ApiResponse(responseCode = "201",
                            description = "Successfully created the category"),
                    @ApiResponse(responseCode = "400",
                            description = "Invalid input")
            }
    )
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public CategoryDto createCategory(@RequestBody @Valid CreateCategoryRequestDto requestDto) {
        return categoryService.createCategory(requestDto);
    }

    @Operation(summary = "Get all categories",
            description = "Provides a set of all categories"
                    + "Supports pagination and sorting",
            parameters = {
                    @Parameter(name = "Page",
                            description = "Number of a page to provide"),
                    @Parameter(name = "Size",
                            description = "Number of categories per page"),
                    @Parameter(name = "Sort",
                            description = "Sorting criteria for the output")
            },
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "Successfully retrieved all categories")
            }
    )
    @GetMapping
    public Page<CategoryDto> getAll(Pageable pageable) {
        return categoryService.findAll(pageable);
    }

    @Operation(summary = "Get a category by id",
            description = "Provides a category by its id",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "Successfully found category by id"),
                    @ApiResponse(responseCode = "400",
                            description = "Invalid ID supplied"),
                    @ApiResponse(responseCode = "404",
                            description = "Category not found")
            }
    )
    @GetMapping("/{id}")
    public CategoryDto getCategoryById(@PathVariable Long id) {
        return categoryService.getCategoryById(id);
    }

    @Operation(summary = "Update a category",
            description = "Updates info about a category",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "Successfully updated the category"),
                    @ApiResponse(responseCode = "400",
                            description = "Invalid ID supplied"),
                    @ApiResponse(responseCode = "404",
                            description = "Category not found")
            }
    )
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public CategoryDto updateCategory(@PathVariable Long id,
                                 @RequestBody @Valid UpdateCategoryRequestDto requestDto) {
        return categoryService.update(id, requestDto);
    }

    @Operation(summary = "Delete a category",
            description = "Marks a category in DB as deleted",
            responses = {
                    @ApiResponse(responseCode = "204",
                            description = "Successfully deleted the category"),
                    @ApiResponse(responseCode = "400",
                            description = "Invalid ID supplied"),
                    @ApiResponse(responseCode = "404",
                            description = "Category not found")
            }
    )
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public void deleteCategory(@PathVariable Long id) {
        categoryService.delete(id);
    }

    @Operation(summary = "Get books by category",
            description = "Provides a list of books "
                    + "that belong to the particular category",
            parameters = {
                    @Parameter(name = "Page",
                            description = "Number of a page to provide"),
                    @Parameter(name = "Size",
                            description = "Number of books per page"),
                    @Parameter(name = "Sort",
                            description = "Sorting criteria for the output")
            },
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "Successfully retrieved all books")
            }
    )
    @GetMapping("/{id}/books")
    public Page<BookDtoWithoutCategoryIds> getBooksByCategoryId(
            @PathVariable Long id,
            Pageable pageable
    ) {
        return bookService.getBooksByCategoryId(id, pageable);
    }
}
