package project.bookstore.dto.shoppingcart;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record UpdateBookQuantityRequestDto(
        @NotNull(message = "Book quantity must be provided")
        @Min(value = 0, message = "Quantity value must be zero or a positive number")
        Integer quantity
) {
}
