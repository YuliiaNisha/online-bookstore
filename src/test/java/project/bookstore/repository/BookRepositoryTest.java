package project.bookstore.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
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
import project.bookstore.util.ControllerRepositoryTestUtil;

@Sql(scripts = {
        "classpath:database/booksCategories/clear-books-categories-table.sql",
        "classpath:database/book/clear-books-table.sql",
        "classpath:database/category/clear-categories-table.sql",
        "classpath:database/category/add-default-category-to-categories-table.sql",
        "classpath:database/book/add-two-default-books-to-books-table.sql",
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
    private PageRequest pageRequest;
    private Book gatsbyBookExpected;
    private Book crimeBookExpected;

    @BeforeEach
    void setUp() {
        gatsbyBookExpected = ControllerRepositoryTestUtil.getGatsbyBookExpected();
        crimeBookExpected = ControllerRepositoryTestUtil.getCrimeBookExpected();
        pageRequest = ControllerRepositoryTestUtil.getDefaultPageRequest();
    }

    @Test
    @DisplayName("Find book by id")
    void findById_validId_returnsBookWithCategories() {
        Optional<Book> optional = bookRepository.findById(DEFAULT_BOOK_ID);

        assertTrue(optional.isPresent());
        Book actual = optional.get();
        assertEquals(gatsbyBookExpected, actual);
    }

    @Test
    @DisplayName("Return all books from DB")
    void findAll_returnsBookPage() {
        Page<Book> actual = bookRepository.findAll(pageRequest);
        assertEquals(getExpectedBooksSet(), getActualBooksSet(actual));
    }

    @Test
    @DisplayName("Return all books from DB according to the Specification")
    void findAllWithSpecification_validId_returnsBookPage() {
        Page<Book> actual = bookRepository.findAll(Specification.where(null), pageRequest);
        assertEquals(getExpectedBooksSet(), getActualBooksSet(actual));
    }

    @Test
    @DisplayName("Find all books by category id")
    void findAllByCategoriesId_validId_returnsBookPage() {
        Page<Book> actual = bookRepository.findAllByCategories_id(
                DEFAULT_CATEGORY_ID, pageRequest
        );

        assertNotNull(actual);
        assertEquals(2, actual.getTotalElements());
        assertEquals(getExpectedBooksSet(), getActualBooksSet(actual));
    }

    private Set<Book> getExpectedBooksSet() {
        return Set.of(
                gatsbyBookExpected,
                crimeBookExpected
        );
    }

    private Set<Book> getActualBooksSet(Page<Book> actual) {
        return new HashSet<>(actual.getContent());
    }
}
