package project.bookstore.service.orderitem;

import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.bookstore.dto.order.OrderItemDto;
import project.bookstore.exception.EntityNotFoundException;
import project.bookstore.mapper.OrderItemMapper;
import project.bookstore.model.Order;
import project.bookstore.model.OrderItem;
import project.bookstore.repository.OrderItemRepository;
import project.bookstore.repository.OrderRepository;

@RequiredArgsConstructor
@Service
public class OrderItemServiceImpl implements OrderItemService {
    private final OrderItemMapper orderItemMapper;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    @Override
    public Set<OrderItemDto> findOrderItemsByOrderId(Long orderId, Long userId) {
        return findOrder(orderId, userId).getOrderItems()
                .stream()
                .map(orderItemMapper::toDto)
                .collect(Collectors.toSet());
    }

    @Override
    public OrderItemDto findOrderItemById(Long orderId, Long itemId, Long userId) {
        checkOrderExistsForUser(orderId, userId);
        return orderItemMapper.toDto(findOrderItem(itemId, orderId));
    }

    private OrderItem findOrderItem(Long itemId, Long orderId) {
        return orderItemRepository.findByIdAndOrderId(itemId, orderId).orElseThrow(
                () -> new EntityNotFoundException(
                        "Can't find order item by id: " + itemId
                                + " in order by id: " + orderId
                )
        );
    }

    private void checkOrderExistsForUser(Long orderId,Long userId) {
        if (!orderRepository.existsByIdAndUserId(orderId, userId)) {
            throw new EntityNotFoundException(
                    "Can't find order with id: " + orderId
                            + " for user with id: " + userId
            );
        }
    }

    private Order findOrder(Long orderId, Long userId) {
        return orderRepository.findByIdAndUserId(orderId, userId).orElseThrow(
                () -> new EntityNotFoundException(
                        "Can't find order by id: " + orderId + " "
                        + " for user with id: " + userId
                )
        );
    }
}
