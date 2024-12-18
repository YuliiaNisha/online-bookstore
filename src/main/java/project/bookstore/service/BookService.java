package project.bookstore.service;

import java.util.List;
import project.bookstore.dto.BookDto;
import project.bookstore.dto.CreateBookRequestDto;

public interface BookService {
    BookDto save(CreateBookRequestDto requestDto);

    BookDto getBookById(Long id);

    List<BookDto> findAll();
}
