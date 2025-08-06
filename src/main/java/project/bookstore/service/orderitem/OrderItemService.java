package project.bookstore.service.orderitem;

import java.util.Set;
import project.bookstore.dto.order.OrderItemDto;

public interface OrderItemService {
    Set<OrderItemDto> findOrderItemsByOrderId(Long orderId, Long userId);

    OrderItemDto findOrderItemById(Long orderId, Long itemId, Long userId);
}
