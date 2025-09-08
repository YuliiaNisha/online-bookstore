package project.bookstore.util;

import java.math.BigDecimal;
import java.util.List;
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

public class ServiceTestUtil {
    public static final Long ID_FIRST = 1L;
    public static final Long ID_SECOND = 2L;

    public static Category getFictionCategory() {
        return new Category()
                .setId(ID_FIRST)
                .setName("Fiction")
                .setDescription("Imaginative storytelling including "
                        + "novels, short stories, and fantasy")
                .setDeleted(false);
    }

    public static Category getFictionCategoryWithoutId() {
        return new Category()
                .setName("Fiction")
                .setDescription("Imaginative storytelling including "
                        + "novels, short stories, and fantasy")
                .setDeleted(false);
    }

    public static Category getFantasyCategory() {
        return new Category()
                .setId(ID_SECOND)
                .setName("Fantasy")
                .setDescription("Magical worlds, mythical creatures, and heroic quests")
                .setDeleted(false);
    }

    public static CreateBookRequestDto getCreateHobbitBookRequestDto() {
        return new CreateBookRequestDto(
                "The Hobbit",
                "J. R. R. Tolkien",
                "978-0547928227",
                BigDecimal.valueOf(14.95),
                "A fantasy novel about Bilbo Baggins’ unexpected "
                        + "journey with dwarves and a wizard to reclaim a stolen treasure.",
                "https://example.com/the-hobbit.jpg",
                Set.of(ID_FIRST, ID_SECOND)
        );
    }

    public static Book getHobbitBookWithoutId() {
        return new Book()
                .setTitle("The Hobbit")
                .setAuthor("J. R. R. Tolkien")
                .setIsbn("978-0547928227")
                .setPrice(BigDecimal.valueOf(14.95))
                .setDescription("A fantasy novel about Bilbo Baggins’ unexpected "
                        + "journey with dwarves and a wizard to reclaim a stolen treasure.")
                .setCoverImage("https://example.com/the-hobbit.jpg")
                .setCategories(Set.of(getFictionCategory(), getFantasyCategory()));
    }

    public static Book getHobbitBookWithId() {
        return new Book()
                .setId(ID_FIRST)
                .setTitle("The Hobbit")
                .setAuthor("J. R. R. Tolkien")
                .setIsbn("978-0547928227")
                .setPrice(BigDecimal.valueOf(14.95))
                .setDescription("A fantasy novel about Bilbo Baggins’ unexpected "
                        + "journey with dwarves and a wizard to reclaim a stolen treasure.")
                .setCoverImage("https://example.com/the-hobbit.jpg")
                .setCategories(Set.of(getFictionCategory(), getFantasyCategory()));
    }

    public static BookDto getHobbitBookDto() {
        return new BookDto(
                ID_FIRST,
                "The Hobbit",
                "J. R. R. Tolkien",
                "978-0547928227",
                BigDecimal.valueOf(14.95),
                "A fantasy novel about Bilbo Baggins’ unexpected "
                        + "journey with dwarves and a wizard to reclaim a stolen treasure.",
                "https://example.com/the-hobbit.jpg",
                Set.of(ID_FIRST, ID_SECOND)
        );
    }

    public static BookDto getHobbitBookUpdatedDto() {
        return new BookDto(
                ID_FIRST,
                "The Hobbit updated",
                "J. R. R. Tolkien updated",
                "978-0547928227111",
                BigDecimal.valueOf(140.95),
                "updated A fantasy novel about Bilbo Baggins’ unexpected "
                        + "journey with dwarves and a wizard to reclaim a stolen treasure.",
                "https://example.com/the-hobbit-updated.jpg",
                Set.of(ID_FIRST)
        );
    }

    public static UpdateBookRequestDto getUpdateHobbitBookRequestDto() {
        return new UpdateBookRequestDto(
                "The Hobbit updated",
                "J. R. R. Tolkien updated",
                "978-0547928227111",
                BigDecimal.valueOf(140.95),
                "updated A fantasy novel about Bilbo Baggins’ unexpected "
                        + "journey with dwarves and a wizard to reclaim a stolen treasure.",
                "https://example.com/the-hobbit-updated.jpg",
                Set.of(ID_FIRST)
        );
    }

    public static BookDtoWithoutCategoryIds getBookDtoWithoutCategoryIds() {
        return new BookDtoWithoutCategoryIds(
                ID_FIRST,
                "The Hobbit",
                "J. R. R. Tolkien",
                "978-0547928227",
                BigDecimal.valueOf(14.95),
                "A fantasy novel about Bilbo Baggins’ unexpected "
                        + "journey with dwarves and a wizard to reclaim a stolen treasure.",
                "https://example.com/the-hobbit.jpg"
        );
    }

    public static List<Book> getDefaultBookList() {
        return List.of(getHobbitBookWithId());
    }

    public static PageRequest getDefaultPageRequest() {
        return PageRequest.of(0, 10);
    }

    public static BookSearchParameters getDefaultBookSearchParameters() {
        return new BookSearchParameters("a", null, null, null);
    }

    public static CreateCategoryRequestDto getCreateFictionCategoryRequestDto() {
        return new CreateCategoryRequestDto(
                "Fiction",
                "Imaginative storytelling including novels, short stories, and fantasy"
        );
    }

    public static CategoryDto getFictionCategoryDto() {
        return new CategoryDto(
                ID_FIRST,
                "Fiction",
                "Imaginative storytelling "
                + "including novels, short stories, and fantasy"
        );
    }

    public static UpdateCategoryRequestDto getUpdateFictionCategoryRequestDto() {
        return new UpdateCategoryRequestDto(
                "Fiction updated",
                "updated Imaginative storytelling "
                        + "including novels, short stories, and fantasy"
        );
    }
}
