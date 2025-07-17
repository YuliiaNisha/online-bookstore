package project.bookstore.service.book;

import java.util.List;
import org.springframework.data.domain.Pageable;
import project.bookstore.dto.book.BookDto;
import project.bookstore.dto.book.BookDtoWithoutCategoryIds;
import project.bookstore.dto.book.BookSearchParameters;
import project.bookstore.dto.book.CreateBookRequestDto;
import project.bookstore.dto.book.UpdateBookRequestDto;

public interface BookService {
    BookDto save(CreateBookRequestDto requestDto);

    BookDto getBookById(Long id);

    List<BookDto> findAll(Pageable pageable);

    void delete(Long id);

    BookDto update(Long id, UpdateBookRequestDto requestDto);

    List<BookDto> search(BookSearchParameters searchParameters, Pageable pageable);

    List<BookDtoWithoutCategoryIds> getBooksByCategoryId(Long id, Pageable pageable);
}
