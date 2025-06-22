package project.bookstore.dto.book;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

@Data
public class CreateBookRequestDto {
    @NotBlank(message = "Title is required. Please provide a title.")
    private String title;
    @NotBlank(message = "Author is required. Please provide an author.")
    private String author;
    @NotBlank(message = "ISBN is required. Please provide a valid ISBN number.")
    private String isbn;
    @NotNull(message = "Price is required. Please provide a price.")
    @Positive(message = "Price must be greater than 0")
    private BigDecimal price;
    @Size(max = 1000, message = "Description must be less than 1000 characters")
    private String description;
    @URL(message = "Invalid cover image URL. Please provide a valid URL.")
    private String coverImage;
}
