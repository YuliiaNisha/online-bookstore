package project.bookstore.dto.order;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;
import project.bookstore.model.OrderItem;

public record OrderDto(
        Long id,
        String firstName,
        String lastName,
        String status,
        BigDecimal total,
        LocalDateTime orderDate,
        String shippingAddress,
        Set<OrderItemDto> orderItemDtos
) {
}
