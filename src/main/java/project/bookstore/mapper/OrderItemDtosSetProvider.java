package project.bookstore.mapper;

import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;
import project.bookstore.dto.order.OrderItemDto;
import project.bookstore.model.OrderItem;

@RequiredArgsConstructor
@Component
public class OrderItemDtosSetProvider {
    private final OrderItemMapper orderItemMapper;

    @Named("getOrderItemDtosSet")
    public Set<OrderItemDto> getOrderItemDtosSet(Set<OrderItem> orderItems) {
        return orderItems.stream()
                .map(orderItemMapper::toDto)
                .collect(Collectors.toSet());
    }
}
