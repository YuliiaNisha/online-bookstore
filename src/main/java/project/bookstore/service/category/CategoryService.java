package project.bookstore.service.category;

import java.util.List;
import org.springframework.data.domain.Pageable;
import project.bookstore.dto.category.CategoryDto;
import project.bookstore.dto.category.CreateCategoryRequestDto;
import project.bookstore.dto.category.UpdateCategoryRequestDto;

public interface CategoryService {
    CategoryDto createCategory(CreateCategoryRequestDto requestDto);

    List<CategoryDto> findAll(Pageable pageable);

    CategoryDto getCategoryById(Long id);

    CategoryDto update(Long id, UpdateCategoryRequestDto requestDto);

    void delete(Long id);
}
