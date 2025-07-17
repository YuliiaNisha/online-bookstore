package project.bookstore.mapper;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;
import project.bookstore.model.Category;
import project.bookstore.repository.CategoryRepository;

@RequiredArgsConstructor
@Component
public class CategoriesCategoryIdsProvider {
    private final CategoryRepository categoryRepository;

    @Named("getCategoriesFromIds")
    public Set<Category> getCategoriesFromIds(
            Set<Long> categoryIds
    ) {
        if (categoryIds.isEmpty()) {
            return new HashSet<>();
        }
        return new HashSet<>(categoryRepository.findAllById(categoryIds));
    }

    @Named("getIdsFromCategories")
    public Set<Long> getCategoryIdsFromCategories(Set<Category> categories) {
        if (categories.isEmpty()) {
            return new HashSet<>();
        }
        return categories.stream()
                .map(Category::getId)
                .collect(Collectors.toSet());
    }

    @Named("updateCategoriesFromIds")
    public Set<Category> updateCategoriesFromIds(
            Set<Long> categoryIds
    ) {
        if (categoryIds == null) {
            return null;
        }
        return new HashSet<>(categoryRepository.findAllById(categoryIds));
    }
}
