package project.bookstore.dto.book;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Set;
import org.hibernate.validator.constraints.URL;

public record UpdateBookRequestDto(
        String title,
        String author,
        String isbn,
        @Positive(message = "Price must be greater than 0")
        BigDecimal price,
        @Size(max = 1000, message = "Description must be less than 1000 characters")
        String description,
        @URL(message = "Invalid cover image URL. Please provide a valid URL.")
        String coverImage,
        Set<Long> categoryIds
){

}
