package project.bookstore.service.order;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import project.bookstore.dto.order.CreateOrderRequestDto;
import project.bookstore.dto.order.OrderDto;
import project.bookstore.dto.order.UpdateOrderStatusRequestDto;
import project.bookstore.model.User;

public interface OrderService {
    OrderDto createOrder(CreateOrderRequestDto requestDto, User user);

    Page<OrderDto> findAllOrders(Long userId, Pageable pageable);

    OrderDto updateStatus(Long orderId, UpdateOrderStatusRequestDto requestDto);
}
