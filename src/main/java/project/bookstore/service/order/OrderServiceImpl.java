package project.bookstore.service.order;

import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import project.bookstore.dto.order.CreateOrderRequestDto;
import project.bookstore.dto.order.OrderDto;
import project.bookstore.dto.order.UpdateOrderStatusRequestDto;
import project.bookstore.exception.EntityNotFoundException;
import project.bookstore.exception.OrderProcessingException;
import project.bookstore.mapper.OrderItemMapper;
import project.bookstore.mapper.OrderMapper;
import project.bookstore.model.CartItem;
import project.bookstore.model.Order;
import project.bookstore.model.OrderItem;
import project.bookstore.model.ShoppingCart;
import project.bookstore.model.User;
import project.bookstore.repository.OrderRepository;
import project.bookstore.repository.ShoppingCartRepository;

@RequiredArgsConstructor
@Service
public class OrderServiceImpl implements OrderService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;

    @Transactional
    @Override
    public OrderDto createOrder(CreateOrderRequestDto requestDto, User user) {
        ShoppingCart userShoppingCart = findShoppingCart(user.getId());
        checkShoppingCartIsEmpty(userShoppingCart);

        Order newOrder = createNewOrder(requestDto, user);
        Set<OrderItem> orderItems = mapOrderItemsFromCartItems(
                userShoppingCart.getCartItems(), newOrder
        );
        newOrder.setOrderItems(orderItems);
        BigDecimal orderTotal = calculateOrderTotal(newOrder.getOrderItems());
        newOrder.setTotal(orderTotal);
        Order savedOrder = orderRepository.save(newOrder);

        userShoppingCart.getCartItems().clear();
        shoppingCartRepository.save(userShoppingCart);

        return orderMapper.toDto(savedOrder);
    }

    @Override
    public Page<OrderDto> findAllOrders(Long userId, Pageable pageable) {
        return orderRepository.findAllByUserId(userId, pageable)
                .map(orderMapper::toDto);
    }

    @Override
    public OrderDto updateStatus(Long orderId, UpdateOrderStatusRequestDto requestDto) {
        Order order = orderRepository.findById(orderId).orElseThrow(
                () -> new EntityNotFoundException(
                        "Can't find order by id: " + orderId
                )
        );
        order.setStatus(Order.Status.valueOf(requestDto.status()));
        return orderMapper.toDto(orderRepository.save(order));
    }

    private void checkShoppingCartIsEmpty(ShoppingCart userShoppingCart) {
        if (userShoppingCart.getCartItems().isEmpty()) {
            throw new OrderProcessingException(
                    "Can't create order. Shopping cart with id: "
                            + userShoppingCart.getId() + " "
                            + "is empty.");
        }
    }

    private Set<OrderItem> mapOrderItemsFromCartItems(Set<CartItem> cartItems, Order newOrder) {
        Set<OrderItem> orderItems = new HashSet<>();
        for (CartItem cartItem : cartItems) {
            OrderItem orderItem = orderItemMapper.fromCartItemToOrderItem(cartItem);
            orderItem.setOrder(newOrder);
            orderItems.add(orderItem);
        }
        return orderItems;
    }

    private ShoppingCart findShoppingCart(Long userId) {
        return shoppingCartRepository.findByUserId(userId).orElseThrow(
                () -> new EntityNotFoundException(
                        "Can't find shopping cart by user id: " + userId
                )
        );
    }

    private Order createNewOrder(CreateOrderRequestDto requestDto, User user) {
        Order newOrder = new Order();
        newOrder.setUser(user);
        newOrder.setStatus(Order.Status.NEW);
        newOrder.setOrderDate(LocalDateTime.now());
        newOrder.setShippingAddress(requestDto.shippingAddress());
        return newOrder;
    }

    private BigDecimal calculateOrderTotal(Set<OrderItem> orderItems) {
        return orderItems.stream()
                .map(item -> item.getPrice().multiply(
                        BigDecimal.valueOf(item.getQuantity())
                ))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
