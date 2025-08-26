package project.bookstore.service.book;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import project.bookstore.dto.book.BookDto;
import project.bookstore.dto.book.BookDtoWithoutCategoryIds;
import project.bookstore.dto.book.BookSearchParameters;
import project.bookstore.dto.book.CreateBookRequestDto;
import project.bookstore.dto.book.UpdateBookRequestDto;
import project.bookstore.exception.EntityNotFoundException;
import project.bookstore.mapper.BookMapper;
import project.bookstore.model.Book;
import project.bookstore.model.Category;
import project.bookstore.repository.BookRepository;
import project.bookstore.repository.CategoryRepository;
import project.bookstore.repository.SpecificationBuilder;

@ExtendWith(MockitoExtension.class)
class BookServiceImplTest {
    public static final long ID = 1L;
    @Mock
    private BookMapper bookMapper;
    @Mock
    private BookRepository bookRepository;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private SpecificationBuilder<Book, BookSearchParameters> bookSpecificationBuilder;

    @InjectMocks
    private BookServiceImpl bookService;

    private final CreateBookRequestDto createHobbitBookRequestDto = createHobbitBookRequestDto();
    private final UpdateBookRequestDto updateHobbitBookRequestDto =
            createUpdateHobbitBookRequestDto();
    private final BookDto hobbitBookDto = createHobbitBookDto();
    private final BookDto hobbitBookUpdatedDto = createHobbitBookUpdatedDto();
    private final BookDtoWithoutCategoryIds hobbitBookDtoWithoutCategoryIds =
            createBookDtoWithoutCategoryIds();
    private final PageRequest defaultPageRequest = PageRequest.of(0, 10);
    private Category fictionCategory;
    private Category fantasyCategory;
    private Book hobbitBookWithoutId;
    private Book hobbitBookWithId;
    private List<Book> defaultBookList;

    @BeforeEach
    void setUp() {
        fictionCategory = new Category()
                .setId(1L)
                .setName("Fiction")
                .setDescription("Imaginative storytelling including "
                        + "novels, short stories, and fantasy")
                .setDeleted(false);
        fantasyCategory = new Category()
                .setId(2L)
                .setName("Fantasy")
                .setDescription("Magical worlds, mythical creatures, and heroic quests")
                .setDeleted(false);
        hobbitBookWithoutId = createHobbitBook(false);
        hobbitBookWithId = createHobbitBook(true);
        defaultBookList = List.of(hobbitBookWithId);
    }

    @Test
    void save_validRequest_returnsBookDto() {
        //given
        when(bookMapper.toModel(createHobbitBookRequestDto)).thenReturn(hobbitBookWithoutId);
        when(bookRepository.save(hobbitBookWithoutId)).thenReturn(hobbitBookWithId);
        when(bookMapper.toDto(hobbitBookWithId)).thenReturn(hobbitBookDto);

        //when
        BookDto actual = bookService.save(createHobbitBookRequestDto);

        //then
        assertNotNull(actual);
        assertEquals(hobbitBookDto, actual);

        verify(bookMapper).toModel(createHobbitBookRequestDto);
        verify(bookRepository).save(hobbitBookWithoutId);
        verify(bookMapper).toDto(hobbitBookWithId);
        verifyNoMoreInteractions(bookMapper, bookRepository);
    }

    @Test
    void getBookById_validId_returnsBookDto() {
        when(bookRepository.findById(ID))
                .thenReturn(Optional.of(hobbitBookWithId));
        when(bookMapper.toDto(hobbitBookWithId)).thenReturn(hobbitBookDto);

        BookDto actual = bookService.getBookById(ID);

        assertNotNull(actual);
        assertEquals(hobbitBookDto, actual);

        verify(bookRepository).findById(ID);
        verify(bookMapper).toDto(hobbitBookWithId);
        verifyNoMoreInteractions(bookRepository,bookMapper);
    }

    @Test
    void getBookById_noBookWithProvidedId_throwsException() {
        when(bookRepository.findById(ID)).thenReturn(Optional.empty());

        EntityNotFoundException actual = assertThrows(
                EntityNotFoundException.class,
                () -> bookService.getBookById(ID)
        );

        assertEquals("Can't find Book by id: " + ID, actual.getMessage());

        verify(bookRepository).findById(ID);
        verifyNoMoreInteractions(bookRepository,bookMapper);
    }

    @Test
    void findAll_providesPageOfBooks() {
        when(bookRepository.findAll(defaultPageRequest))
                .thenReturn(new PageImpl<>(defaultBookList));
        when(bookMapper.toDto(hobbitBookWithId)).thenReturn(hobbitBookDto);

        Page<BookDto> actual = bookService.findAll(defaultPageRequest);

        assertNotNull(actual);
        assertEquals(1, actual.getTotalElements());
        assertEquals(hobbitBookDto, actual.getContent().get(0));

        verify(bookRepository).findAll(defaultPageRequest);
        verify(bookMapper).toDto(hobbitBookWithId);
        verifyNoMoreInteractions(bookRepository, bookMapper);
    }

    @Test
    void delete_validId_removesBookFromDb() {
        when(bookRepository.findById(ID))
                .thenReturn(Optional.of(hobbitBookWithId));

        bookService.delete(ID);

        verify(bookRepository).findById(ID);
        verify(bookRepository).delete(hobbitBookWithId);
        verifyNoMoreInteractions(bookRepository);
    }

    @Test
    void delete_noBookWithProvidedId_throwsException() {
        when(bookRepository.findById(ID))
                .thenReturn(Optional.empty());

        EntityNotFoundException actual = assertThrows(
                EntityNotFoundException.class,
                () -> bookService.delete(ID));

        assertEquals("There is no book by id: " + ID, actual.getMessage());

        verify(bookRepository).findById(ID);
        verifyNoMoreInteractions(bookRepository);
    }

    @Test
    void update_validIdAndRequestDto_returnsBookDto() {
        when(bookRepository.findById(ID))
                .thenReturn(Optional.of(hobbitBookWithId));
        when(bookRepository.save(hobbitBookWithId))
                .thenReturn(hobbitBookWithId);
        when(bookMapper.toDto(hobbitBookWithId)).thenReturn(hobbitBookUpdatedDto);

        BookDto actual = bookService.update(ID, updateHobbitBookRequestDto);

        assertNotNull(actual);
        assertEquals(hobbitBookUpdatedDto, actual);

        verify(bookRepository).findById(ID);
        verify(bookMapper).updateModelFromDto(hobbitBookWithId, updateHobbitBookRequestDto);
        verify(bookRepository).save(hobbitBookWithId);
        verify(bookMapper).toDto(hobbitBookWithId);
        verifyNoMoreInteractions(bookRepository, bookMapper);
    }

    @Test
    void update_noBookWithProvidedId_throwsException() {
        when(bookRepository.findById(ID))
                .thenReturn(Optional.empty());

        EntityNotFoundException actual = assertThrows(
                EntityNotFoundException.class,
                () -> bookService.update(ID, updateHobbitBookRequestDto)
        );

        assertEquals("There is no book by id: " + ID, actual.getMessage());

        verify(bookRepository).findById(ID);
        verifyNoMoreInteractions(bookRepository, bookMapper);
    }

    @Test
    void search_validRequest_providesPageOfBooks() {
        BookSearchParameters bookSearchParameters = createBookSearchParameters();
        when(bookSpecificationBuilder.build(bookSearchParameters))
                .thenReturn(Specification.where(null));
        when(bookRepository.findAll(any(Specification.class), eq(defaultPageRequest)))
                .thenReturn(new PageImpl<>(defaultBookList));
        when(bookMapper.toDto(hobbitBookWithId)).thenReturn(hobbitBookDto);

        Page<BookDto> actual = bookService.search(bookSearchParameters, defaultPageRequest);

        assertNotNull(actual);
        assertEquals(1, actual.getTotalElements());
        assertEquals(hobbitBookDto, actual.getContent().get(0));

        verify(bookSpecificationBuilder).build(bookSearchParameters);
        verify(bookRepository).findAll(any(Specification.class), eq(defaultPageRequest));
        verify(bookMapper).toDto(hobbitBookWithId);
        verifyNoMoreInteractions(bookSpecificationBuilder, bookRepository, bookMapper);
    }

    @Test
    void getBooksByCategoryId_validId_providesPageOfBooks() {
        when(categoryRepository.findById(ID))
                .thenReturn(Optional.of(fictionCategory));
        when(bookRepository.findAllByCategories_id(ID, defaultPageRequest))
                .thenReturn(new PageImpl<>(defaultBookList));
        when(bookMapper.toDtoWithoutCategories(hobbitBookWithId))
                .thenReturn(hobbitBookDtoWithoutCategoryIds);

        Page<BookDtoWithoutCategoryIds> actual =
                bookService.getBooksByCategoryId(ID, defaultPageRequest);

        assertNotNull(actual);
        assertEquals(1, actual.getTotalElements());
        assertEquals(hobbitBookDtoWithoutCategoryIds, actual.getContent().get(0));

        verify(categoryRepository).findById(ID);
        verify(bookRepository).findAllByCategories_id(ID, defaultPageRequest);
        verify(bookMapper).toDtoWithoutCategories(hobbitBookWithId);
        verifyNoMoreInteractions(categoryRepository, bookRepository, bookMapper);
    }

    @Test
    void getBooksByCategoryId_noCategoryById_throwsException() {
        when(categoryRepository.findById(ID))
                .thenReturn(Optional.empty());

        EntityNotFoundException actual = assertThrows(
                EntityNotFoundException.class,
                () -> bookService.getBooksByCategoryId(ID, defaultPageRequest)
        );

        assertNotNull(actual);
        assertEquals("There is no category by id: " + ID, actual.getMessage());

        verify(categoryRepository).findById(ID);
        verifyNoMoreInteractions(categoryRepository, bookRepository, bookMapper);
    }

    private BookDto createHobbitBookDto() {
        return new BookDto(
                1L,
                "The Hobbit",
                "J. R. R. Tolkien",
                "978-0547928227",
                BigDecimal.valueOf(14.95),
                "A fantasy novel about Bilbo Baggins’ unexpected "
                        + "journey with dwarves and a wizard to reclaim a stolen treasure.",
                "https://example.com/the-hobbit.jpg",
                Set.of(1L, 2L)
        );
    }

    private CreateBookRequestDto createHobbitBookRequestDto() {
        return new CreateBookRequestDto(
                "The Hobbit",
                "J. R. R. Tolkien",
                "978-0547928227",
                BigDecimal.valueOf(14.95),
                "A fantasy novel about Bilbo Baggins’ unexpected "
                        + "journey with dwarves and a wizard to reclaim a stolen treasure.",
                "https://example.com/the-hobbit.jpg",
                Set.of(1L, 2L)
        );
    }

    private Book createHobbitBook(boolean idExists) {
        Book book = new Book()
                .setTitle("The Hobbit")
                .setAuthor("J. R. R. Tolkien")
                .setIsbn("978-0547928227")
                .setPrice(BigDecimal.valueOf(14.95))
                .setDescription("A fantasy novel about Bilbo Baggins’ unexpected "
                        + "journey with dwarves and a wizard to reclaim a stolen treasure.")
                .setCoverImage("https://example.com/the-hobbit.jpg")
                .setCategories(Set.of(fictionCategory, fantasyCategory));
        if (idExists) {
            book.setId(1L);
        }
        return book;
    }

    private UpdateBookRequestDto createUpdateHobbitBookRequestDto() {
        return new UpdateBookRequestDto(
                "The Hobbit updated",
                "J. R. R. Tolkien updated",
                "978-0547928227111",
                BigDecimal.valueOf(140.95),
                "updated A fantasy novel about Bilbo Baggins’ unexpected "
                        + "journey with dwarves and a wizard to reclaim a stolen treasure.",
                "https://example.com/the-hobbit-updated.jpg",
                Set.of(1L)
        );
    }

    private BookDto createHobbitBookUpdatedDto() {
        return new BookDto(
                1L,
                "The Hobbit updated",
                "J. R. R. Tolkien updated",
                "978-0547928227111",
                BigDecimal.valueOf(140.95),
                "updated A fantasy novel about Bilbo Baggins’ unexpected "
                        + "journey with dwarves and a wizard to reclaim a stolen treasure.",
                "https://example.com/the-hobbit-updated.jpg",
                Set.of(1L)
        );
    }

    private BookSearchParameters createBookSearchParameters() {
        return new BookSearchParameters("a", null, null, null);
    }

    private BookDtoWithoutCategoryIds createBookDtoWithoutCategoryIds() {
        return new BookDtoWithoutCategoryIds(
                1L,
                "The Hobbit",
                "J. R. R. Tolkien",
                "978-0547928227",
                BigDecimal.valueOf(14.95),
                "A fantasy novel about Bilbo Baggins’ unexpected "
                        + "journey with dwarves and a wizard to reclaim a stolen treasure.",
                "https://example.com/the-hobbit.jpg"
        );
    }
}
