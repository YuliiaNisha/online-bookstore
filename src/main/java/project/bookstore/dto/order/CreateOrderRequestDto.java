package project.bookstore.dto.order;

import jakarta.validation.constraints.NotNull;

public record CreateOrderRequestDto(
        @NotNull
        String shippingAddress
) {
}
