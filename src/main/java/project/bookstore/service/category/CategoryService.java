package project.bookstore.service.category;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import project.bookstore.dto.category.CategoryDto;
import project.bookstore.dto.category.CreateCategoryRequestDto;
import project.bookstore.dto.category.UpdateCategoryRequestDto;

public interface CategoryService {
    CategoryDto createCategory(CreateCategoryRequestDto requestDto);

    Page<CategoryDto> findAll(Pageable pageable);

    CategoryDto getCategoryById(Long id);

    CategoryDto update(Long id, UpdateCategoryRequestDto requestDto);

    void delete(Long id);
}
