package project.bookstore.service;

import java.util.List;
import org.springframework.data.domain.Pageable;
import project.bookstore.dto.BookDto;
import project.bookstore.dto.BookSearchParameters;
import project.bookstore.dto.CreateBookRequestDto;

public interface BookService {
    BookDto save(CreateBookRequestDto requestDto);

    BookDto getBookById(Long id);

    List<BookDto> findAll(Pageable pageable);

    void delete(Long id);

    BookDto update(Long id, CreateBookRequestDto requestDto);

    List<BookDto> search(BookSearchParameters searchParameters, Pageable pageable);
}
