package project.bookstore.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import project.bookstore.config.MapperConfig;
import project.bookstore.dto.category.CategoryDto;
import project.bookstore.dto.category.CreateCategoryRequestDto;
import project.bookstore.dto.category.UpdateCategoryRequestDto;
import project.bookstore.model.Category;

@Mapper(config = MapperConfig.class)
public interface CategoryMapper {
    Category toModel(CreateCategoryRequestDto requestDto);

    CategoryDto toDto(Category category);

    void updateCategory(@MappingTarget Category category, UpdateCategoryRequestDto requestDto);
}
