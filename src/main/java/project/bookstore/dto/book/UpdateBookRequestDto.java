package project.bookstore.dto.book;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

@Data
public class UpdateBookRequestDto {
    private String title;
    private String author;
    private String isbn;
    @Positive(message = "Price must be greater than 0")
    private BigDecimal price;
    @Size(max = 1000, message = "Description must be less than 1000 characters")
    private String description;
    @URL(message = "Invalid cover image URL. Please provide a valid URL.")
    private String coverImage;
}
