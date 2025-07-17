package project.bookstore.service.category;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import project.bookstore.dto.category.CategoryDto;
import project.bookstore.dto.category.CreateCategoryRequestDto;
import project.bookstore.dto.category.UpdateCategoryRequestDto;
import project.bookstore.exception.EntityNotFoundException;
import project.bookstore.mapper.CategoryMapper;
import project.bookstore.model.Category;
import project.bookstore.repository.CategoryRepository;

@RequiredArgsConstructor
@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryMapper categoryMapper;
    private final CategoryRepository categoryRepository;

    @Override
    public CategoryDto createCategory(CreateCategoryRequestDto requestDto) {
        Category model = categoryMapper.toModel(requestDto);
        return categoryMapper.toDto(categoryRepository.save(model));
    }

    @Override
    public List<CategoryDto> findAll(Pageable pageable) {
        return categoryRepository.findAll(pageable)
                .stream()
                .map(categoryMapper::toDto)
                .toList();
    }

    @Override
    public CategoryDto getCategoryById(Long id) {
        Category category = getCategoryModel(id);
        return categoryMapper.toDto(category);
    }

    @Override
    public CategoryDto update(Long id, UpdateCategoryRequestDto requestDto) {
        Category category = getCategoryModel(id);
        categoryMapper.updateCategory(category, requestDto);
        return categoryMapper.toDto(categoryRepository.save(category));
    }

    @Override
    public void delete(Long id) {
        Category category = getCategoryModel(id);
        categoryRepository.delete(category);
    }

    private Category getCategoryModel(Long id) {
        return categoryRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(
                        "There is no category by id: " + id
                )
        );
    }
}
