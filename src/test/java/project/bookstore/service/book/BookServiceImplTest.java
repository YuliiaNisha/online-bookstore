package project.bookstore.service.book;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeAll;
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
import project.bookstore.util.ServiceTestUtil;

@ExtendWith(MockitoExtension.class)
class BookServiceImplTest {
    public static final Long ID = 1L;
    private static BookDto hobbitBookDto;
    private static BookDto hobbitBookUpdatedDto;
    private static BookDtoWithoutCategoryIds hobbitBookDtoWithoutCategoryIds;
    private static CreateBookRequestDto requestDto;
    private static PageRequest defaultPageRequest;
    private static UpdateBookRequestDto updateHobbitBookRequestDto;
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
    private Book hobbitBookWithoutId;
    private Book hobbitBookWithId;
    private Category fictionCategory;
    private List<Book> defaultBookList;
    private BookSearchParameters defaultBookSearchParameters;

    @BeforeAll
    static void beforeAll() {
        requestDto = ServiceTestUtil.getCreateHobbitBookRequestDto();
        hobbitBookDto = ServiceTestUtil.getHobbitBookDto();
        hobbitBookUpdatedDto = ServiceTestUtil.getHobbitBookUpdatedDto();
        updateHobbitBookRequestDto = ServiceTestUtil.getUpdateHobbitBookRequestDto();
        defaultPageRequest = ServiceTestUtil.getDefaultPageRequest();
        hobbitBookDtoWithoutCategoryIds =
                ServiceTestUtil.getBookDtoWithoutCategoryIds();
    }

    @BeforeEach
    void setUp() {
        hobbitBookWithoutId = ServiceTestUtil.getHobbitBookWithoutId();
        hobbitBookWithId = ServiceTestUtil.getHobbitBookWithId();
        fictionCategory = ServiceTestUtil.getFictionCategory();
        defaultBookList = ServiceTestUtil.getDefaultBookList();
        defaultBookSearchParameters = ServiceTestUtil.getDefaultBookSearchParameters();
    }

    @Test
    void save_validRequest_returnsBookDto() {
        when(bookMapper.toModel(requestDto)).thenReturn(hobbitBookWithoutId);
        when(bookRepository.save(hobbitBookWithoutId)).thenReturn(hobbitBookWithId);
        when(bookMapper.toDto(hobbitBookWithId)).thenReturn(hobbitBookDto);

        BookDto actual = bookService.save(requestDto);

        assertNotNull(actual);
        assertEquals(hobbitBookDto, actual);

        verify(bookMapper).toModel(requestDto);
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
        when(bookSpecificationBuilder.build(defaultBookSearchParameters))
                .thenReturn(Specification.where(null));
        when(bookRepository.findAll(any(Specification.class), eq(defaultPageRequest)))
                .thenReturn(new PageImpl<>(defaultBookList));
        when(bookMapper.toDto(hobbitBookWithId)).thenReturn(hobbitBookDto);

        Page<BookDto> actual = bookService.search(defaultBookSearchParameters, defaultPageRequest);

        assertNotNull(actual);
        assertEquals(1, actual.getTotalElements());
        assertEquals(hobbitBookDto, actual.getContent().get(0));

        verify(bookSpecificationBuilder).build(defaultBookSearchParameters);
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
                () -> bookService.getBooksByCategoryId(
                        ID, defaultPageRequest
                )
        );

        assertNotNull(actual);
        assertEquals("There is no category by id: " + ID, actual.getMessage());

        verify(categoryRepository).findById(ID);
        verifyNoMoreInteractions(categoryRepository, bookRepository, bookMapper);
    }
}
