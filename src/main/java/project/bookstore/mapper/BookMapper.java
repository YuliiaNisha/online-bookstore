package project.bookstore.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import project.bookstore.config.MapperConfig;
import project.bookstore.dto.BookDto;
import project.bookstore.dto.CreateBookRequestDto;
import project.bookstore.model.Book;

@Mapper(config = MapperConfig.class)
public interface BookMapper {
    BookDto toDto(Book book);

    Book toModel(CreateBookRequestDto requestDto);

    void updateModelFromDto(@MappingTarget Book book, CreateBookRequestDto requestDto);
}
