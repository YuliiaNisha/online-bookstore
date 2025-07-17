package project.bookstore.dto.category;

import jakarta.validation.constraints.NotBlank;

public record CreateCategoryRequestDto(
        @NotBlank(message = "Category name is required")
        String name,
        @NotBlank(message = "Category description is required")
        String description
) {
}
