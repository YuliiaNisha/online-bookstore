package project.bookstore.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import project.bookstore.config.MapperConfig;
import project.bookstore.dto.book.BookDto;
import project.bookstore.dto.book.BookDtoWithoutCategoryIds;
import project.bookstore.dto.book.CreateBookRequestDto;
import project.bookstore.dto.book.UpdateBookRequestDto;
import project.bookstore.model.Book;

@Mapper(config = MapperConfig.class, uses = CategoriesCategoryIdsProvider.class)
public interface BookMapper {
    @Mapping(source = "categories", target = "categoryIds",
            qualifiedByName = "getIdsFromCategories")
    BookDto toDto(Book book);

    @Mapping(source = "categoryIds", target = "categories",
            qualifiedByName = "getCategoriesFromIds")
    Book toModel(CreateBookRequestDto requestDto);

    @Mapping(source = "categoryIds", target = "categories",
            qualifiedByName = "updateCategoriesFromIds")
    void updateModelFromDto(@MappingTarget Book book, UpdateBookRequestDto requestDto);

    BookDtoWithoutCategoryIds toDtoWithoutCategories(Book book);
}
