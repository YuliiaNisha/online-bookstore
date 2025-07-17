package project.bookstore.service.book;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import project.bookstore.dto.book.BookDto;
import project.bookstore.dto.book.BookDtoWithoutCategoryIds;
import project.bookstore.dto.book.BookSearchParameters;
import project.bookstore.dto.book.CreateBookRequestDto;
import project.bookstore.dto.book.UpdateBookRequestDto;
import project.bookstore.exception.EntityNotFoundException;
import project.bookstore.mapper.BookMapper;
import project.bookstore.model.Book;
import project.bookstore.repository.BookRepository;
import project.bookstore.repository.CategoryRepository;
import project.bookstore.repository.SpecificationBuilder;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final CategoryRepository categoryRepository;
    private final SpecificationBuilder<Book, BookSearchParameters> bookSpecificationBuilder;

    @Override
    public BookDto save(CreateBookRequestDto requestDto) {
        Book modelBook = bookMapper.toModel(requestDto);
        return bookMapper.toDto(bookRepository.save(modelBook));
    }

    @Override
    public BookDto getBookById(Long id) {
        Book book = bookRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can't find Book by id: " + id)
        );
        return bookMapper.toDto(book);
    }

    @Override
    public List<BookDto> findAll(Pageable pageable) {
        return bookRepository.findAll(pageable)
                .stream()
                .map(bookMapper::toDto)
                .toList();
    }

    @Override
    public void delete(Long id) {
        Book book = bookRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("There is no book by id: " + id)
        );
        bookRepository.delete(book);
    }

    @Override
    public BookDto update(Long id, UpdateBookRequestDto requestDto) {
        Book book = bookRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("There is no book by id: " + id)
        );
        bookMapper.updateModelFromDto(book, requestDto);
        return bookMapper.toDto(bookRepository.save(book));
    }

    @Override
    public List<BookDto> search(BookSearchParameters searchParameters, Pageable pageable) {
        Specification<Book> bookSpecification = bookSpecificationBuilder.build(searchParameters);
        return bookRepository.findAll(bookSpecification, pageable)
                .stream()
                .map(bookMapper::toDto)
                .toList();
    }

    @Override
    public List<BookDtoWithoutCategoryIds> getBooksByCategoryId(Long id, Pageable pageable) {
        categoryRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(
                        "There is no category by id: " + id
                )
        );
        return bookRepository.findAllByCategories_id(id, pageable)
                .stream()
                .map(bookMapper::toDtoWithoutCategories)
                .toList();
    }
}
