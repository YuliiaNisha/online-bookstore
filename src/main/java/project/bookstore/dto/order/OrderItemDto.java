package project.bookstore.dto.order;

import java.math.BigDecimal;

public record OrderItemDto(
        Long id,
        Long orderId,
        String bookTitle,
        String bookAuthor,
        int quantity,
        BigDecimal price
) {
}
