package project.bookstore.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import project.bookstore.dto.BookDto;
import project.bookstore.dto.BookSearchParameters;
import project.bookstore.dto.CreateBookRequestDto;
import project.bookstore.service.BookService;

@Tag(name = "Bookstore API",
        description = "Endpoints for managing books")
@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;

    @Operation(summary = "Create a new book",
            description = "Adds a new book to DB",
            responses = {
                    @ApiResponse(responseCode = "200",
                    description = "Successfully created the book")
            }
    )
    @PostMapping
    public BookDto createBook(@RequestBody @Valid CreateBookRequestDto requestDto) {
        return bookService.save(requestDto);
    }

    @Operation(summary = "Get a book by id",
            description = "Provides a book by its id",
            responses = {
                    @ApiResponse(responseCode = "200",
                    description = "Successfully found book by id")
            }
    )
    @GetMapping("/{id}")
    public BookDto getBookById(@PathVariable Long id) {
        return bookService.getBookById(id);
    }

    @Operation(summary = "Get all books",
            description = "Provides a list of all books. "
                    + "Supports pagination and sorting",
            parameters = {
                    @Parameter(name = "Page",
                            description = "Number of a page to provide"),
                    @Parameter(name = "Size",
                            description = "Number of book per page"),
                    @Parameter(name = "Sort",
                            description = "Sorting criteria for the output")
            },
            responses = {
                    @ApiResponse(responseCode = "200",
                    description = "Successfully retrieved all books")
            }
    )
    @GetMapping
    public List<BookDto> getAll(Pageable pageable) {
        return bookService.findAll(pageable);
    }

    @Operation(summary = "Delete a book",
            description = "Marks a book in DB as deleted",
            responses = {
                    @ApiResponse(responseCode = "200",
                    description = "Successfully deleted the book")
            }
    )
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        bookService.delete(id);
    }

    @Operation(summary = "Update a book",
            description = "Updates info about a book",
            responses = {
                    @ApiResponse(responseCode = "200",
                    description = "Successfully updated the book")
            }
    )
    @PutMapping("/{id}")
    public BookDto update(@PathVariable Long id,
                          @RequestBody @Valid CreateBookRequestDto requestDto) {
        return bookService.update(id, requestDto);
    }

    @Operation(summary = "Search for books",
            description = "Provides a list of books according to search parameters "
                    + "such as title, author, isbn, or description. "
                    + "Supports pagination and sorting.",
            parameters = {
                    @Parameter(name = "Title Part",
                            description = "Title or its part to search for"),
                    @Parameter(name = "Author",
                            description = "Author to search for"),
                    @Parameter(name = "Isbn",
                            description = "Isbn to search for"),
                    @Parameter(name = "Description Part",
                            description = "Description or its part to search for"),
                    @Parameter(name = "Page",
                            description = "Number of a page to provide"),
                    @Parameter(name = "Size",
                            description = "Number of book per page"),
                    @Parameter(name = "Sort",
                            description = "Sorting criteria for the output")
            },
            responses = {
                    @ApiResponse(responseCode = "200",
                    description = "Successfully retrieved books")
            }
    )
    @GetMapping("/search")
    public List<BookDto> search(BookSearchParameters searchParameters, Pageable pageable) {
        return bookService.search(searchParameters, pageable);
    }
}
