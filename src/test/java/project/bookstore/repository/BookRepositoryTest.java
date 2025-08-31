package project.bookstore.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.jdbc.Sql;
import project.bookstore.model.Book;
import project.bookstore.model.Category;

@Sql(scripts = {
        "classpath:database/booksCategories/clear-books-categories-table.sql",
        "classpath:database/book/clear-books-table.sql",
        "classpath:database/category/clear-categories-table.sql",
        "classpath:database/category/add-default-category-to-categories-table.sql",
        "classpath:database/booksCategories/add-two-default-books-to-books-table.sql",
        "classpath:database/booksCategories/insert-into-books-categories.sql",
},
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BookRepositoryTest {
    public static final Long DEFAULT_CATEGORY_ID = 1L;
    public static final Long DEFAULT_BOOK_ID = 1L;
    @Autowired
    private BookRepository bookRepository;
    private final PageRequest pageRequest = PageRequest.of(0, 10);

    @Test
    @DisplayName("Find book by id")
    void findById_validId_returnsBookWithCategories() {
        Optional<Book> actual = bookRepository.findById(DEFAULT_BOOK_ID);

        assertTrue(actual.isPresent());
        Book book = actual.get();
        assertEquals("The Great Gatsby", book.getTitle());
        assertEquals("9780743273565", book.getIsbn());
        assertEquals(0, book.getPrice().compareTo(BigDecimal.valueOf(15.99)));
        assertHasDefaultCategory(book);
    }

    @Test
    @DisplayName("Return all books from DB")
    void findAll_returnsBookPage() {
        Page<Book> actual = bookRepository.findAll(pageRequest);
        assertBooksContainExpectedValues(actual);
    }

    @Test
    @DisplayName("Return all books from DB according to the Specification")
    void findAllWithSpecification_validId_returnsBookPage() {
        Page<Book> actual = bookRepository.findAll(Specification.where(null), pageRequest);
        assertBooksContainExpectedValues(actual);
    }

    @Test
    @DisplayName("Find all books by category id")
    void findAllByCategoriesId_validId_returnsBookPage() {
        Page<Book> actual = bookRepository.findAllByCategories_id(
                DEFAULT_CATEGORY_ID, pageRequest
        );

        assertNotNull(actual);
        assertEquals(2, actual.getTotalElements());
        assertNotNull(actual.getContent().get(0).getCategories());
        assertHasDefaultCategory(actual.getContent().get(0));
        assertNotNull(actual.getContent().get(1).getCategories());
        assertHasDefaultCategory(actual.getContent().get(1));
    }

    private void assertHasDefaultCategory(Book book) {
        assertEquals(
                Set.of("Default category"),
                book.getCategories()
                        .stream()
                        .map(Category::getName)
                        .collect(Collectors.toSet()));
    }

    private void assertBooksContainExpectedValues(Page<Book> actual) {
        assertNotNull(actual);
        assertFalse(actual.isEmpty());
        assertEquals(2, actual.getTotalElements());
        actual.getContent().forEach(
                this::assertHasDefaultCategory
        );
        Set<String> actualTitlesSet = actual.getContent()
                .stream()
                .map(Book::getTitle)
                .collect(Collectors.toSet());
        assertEquals(
                Set.of("The Great Gatsby", "1984"),
                actualTitlesSet);
    }
}
