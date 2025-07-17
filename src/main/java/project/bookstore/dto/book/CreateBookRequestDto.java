package project.bookstore.dto.book;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Set;
import org.hibernate.validator.constraints.URL;

public record CreateBookRequestDto(
        @NotBlank(message = "Title is required. Please provide a title.")
        String title,
        @NotBlank(message = "Author is required. Please provide an author.")
        String author,

        @NotBlank(message = "ISBN is required. Please provide a valid ISBN number.")
        String isbn,

        @NotNull(message = "Price is required. Please provide a price.")
        @Positive(message = "Price must be greater than 0")
        BigDecimal price,

        @Size(max = 1000, message = "Description must be less than 1000 characters")
        String description,

        @URL(message = "Invalid cover image URL. Please provide a valid URL.")
        String coverImage,

        @NotNull(message = "Categories are required. Please provide book categories.")
        Set<Long> categoryIds
) {
}
