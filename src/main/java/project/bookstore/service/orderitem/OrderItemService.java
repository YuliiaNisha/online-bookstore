package project.bookstore.service.orderitem;

import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import project.bookstore.dto.order.OrderItemDto;

public interface OrderItemService {
    Set<OrderItemDto> findOrderItemsByOrderId(Long orderId, Long userId);

    OrderItemDto findOrderItemById(Long orderId, Long itemId, Long userId);
}
