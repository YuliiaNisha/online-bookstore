package project.bookstore.dto.order;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

public record OrderDto(
        Long id,
        Long userId,
        String status,
        BigDecimal total,
        LocalDateTime orderDate,
        Set<OrderItemDto> orderItemDtos
) {
}
