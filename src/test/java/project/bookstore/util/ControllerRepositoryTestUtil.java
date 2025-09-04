package project.bookstore.util;

import java.math.BigDecimal;
import java.util.Set;
import org.springframework.data.domain.PageRequest;
import project.bookstore.dto.book.BookDto;
import project.bookstore.dto.book.BookDtoWithoutCategoryIds;
import project.bookstore.dto.book.BookSearchParameters;
import project.bookstore.dto.book.CreateBookRequestDto;
import project.bookstore.dto.book.UpdateBookRequestDto;
import project.bookstore.dto.category.CategoryDto;
import project.bookstore.dto.category.CreateCategoryRequestDto;
import project.bookstore.dto.category.UpdateCategoryRequestDto;
import project.bookstore.model.Book;
import project.bookstore.model.Category;

public class ControllerRepositoryTestUtil {
    public static final Long ID_FIRST = 1L;
    public static final Long ID_SECOND = 2L;

    public static BookDto getGatsbyBookDtoExpected() {
        return new BookDto(
                ID_FIRST,
                "The Great Gatsby",
                "F. Scott Fitzgerald",
                "9780743273565",
                BigDecimal.valueOf(15.99),
                "Classic novel set in the 1920s",
                "great_gatsby.jpg",
                Set.of(ID_FIRST)
        );
    }

    public static BookDto getUpdatedGatsbyBookDtoExpected() {
        return new BookDto(
                ID_FIRST,
                "The Great Gatsby updated",
                "F. Scott Fitzgerald",
                "9780743273565",
                BigDecimal.valueOf(15.99),
                "Classic novel set in the 1920s",
                "great_gatsby.jpg",
                Set.of(ID_FIRST)
        );
    }

    public static BookDto getCrimeBookDtoExpected() {
        return new BookDto(
                ID_SECOND,
                "Crime and Punishment",
                "Fyodor Dostoevsky",
                "9780143058144",
                BigDecimal.valueOf(15.25),
                "A psychological drama",
                "/images/crime-punishment.jpg",
                Set.of(ID_FIRST)
        );
    }

    public static BookDto getPotterBookDtoExpected() {
        return new BookDto(
                ID_FIRST,
                "Harry Potter and the Philosopher's Stone",
                "J. K. Rowling",
                "978-0747532699",
                BigDecimal.valueOf(12.50),
                "The first book in the Harry Potter series, "
                        + "following a young wizard discovering his magical heritage.",
                "https://example.com/harry-potter.jpg",
                Set.of(ID_FIRST)
        );
    }

    public static CreateBookRequestDto getCreatePotterBookRequest() {
        return new CreateBookRequestDto(
                "Harry Potter and the Philosopher's Stone",
                "J. K. Rowling",
                "978-0747532699",
                BigDecimal.valueOf(12.50),
                "The first book in the Harry Potter series, "
                        + "following a young wizard discovering his magical heritage.",
                "https://example.com/harry-potter.jpg",
                Set.of(ID_FIRST)
        );
    }

    public static UpdateBookRequestDto getUpdateGatsbyBookRequest() {
        return new UpdateBookRequestDto(
                "The Great Gatsby updated",
                null,
                null,
                null,
                null,
                null,
                null
        );
    }

    public static BookSearchParameters getGatsbyBookSearchParameters() {
        return new BookSearchParameters(
                "Gatsby",
                null,
                null,
                null
        );
    }

    public static CreateCategoryRequestDto getCreateTestCategoryRequest() {
        return new CreateCategoryRequestDto(
                "Test category",
                "Test category Description"
        );
    }

    public static CategoryDto getTestCategoryDtoExpected() {
        return new CategoryDto(
                3L,
                "Test category",
                "Test category Description"
        );
    }

    public static CategoryDto getFictionCategoryDtoExpected() {
        return new CategoryDto(
                ID_FIRST,
                "Fiction",
                "Fiction Description"
        );
    }

    public static CategoryDto getUpdatedFictionCategoryDtoExpected() {
        return new CategoryDto(
                ID_FIRST,
                "Fiction updated",
                "Fiction Description"
        );
    }

    public static CategoryDto getComedyCategoryDtoExpected() {
        return new CategoryDto(
                ID_SECOND,
                "Comedy",
                "Comedy Description"
        );
    }

    public static UpdateCategoryRequestDto getUpdateFictionCategoryRequest() {
        return new UpdateCategoryRequestDto(
                "Fiction updated",
                null
        );
    }

    public static UpdateCategoryRequestDto getEmptyUpdateCategoryRequest() {
        return new UpdateCategoryRequestDto(
                null,
                null
        );
    }

    public static Book getGatsbyBookExpected() {
        return new Book()
                .setId(ID_FIRST)
                .setTitle("The Great Gatsby")
                .setAuthor("F. Scott Fitzgerald")
                .setIsbn("9780743273565")
                .setPrice(BigDecimal.valueOf(15.99))
                .setDescription("Classic novel set in the 1920s")
                .setCoverImage("great_gatsby.jpg")
                .setCategories(
                        Set.of(
                                new Category()
                                        .setId(ID_FIRST)
                                        .setName("Fiction")
                                        .setDescription("Fiction Description")
                                        .setDeleted(false)
                        )
                );
    }

    public static Book getCrimeBookExpected() {
        return new Book()
                .setId(ID_SECOND)
                .setTitle("Crime and Punishment")
                .setAuthor("Fyodor Dostoevsky")
                .setIsbn("9780143058144")
                .setPrice(BigDecimal.valueOf(15.25))
                .setDescription("A psychological drama")
                .setCoverImage("/images/crime-punishment.jpg")
                .setCategories(
                        Set.of(
                                new Category()
                                        .setId(ID_FIRST)
                                        .setName("Fiction")
                                        .setDescription("Fiction Description")
                                        .setDeleted(false)
                        )
                );
    }

    public static BookDtoWithoutCategoryIds getGatsbyWithoutCategoryIdsExpected() {
        return new BookDtoWithoutCategoryIds(
                ID_FIRST,
                "The Great Gatsby",
                "F. Scott Fitzgerald",
                "9780743273565",
                BigDecimal.valueOf(15.99),
                "Classic novel set in the 1920s",
                "great_gatsby.jpg"
        );
    }

    public static PageRequest getDefaultPageRequest() {
        return PageRequest.of(0, 10);
    }
}
