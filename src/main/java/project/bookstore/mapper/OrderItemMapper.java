package project.bookstore.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import project.bookstore.config.MapperConfig;
import project.bookstore.dto.order.OrderItemDto;
import project.bookstore.model.CartItem;
import project.bookstore.model.OrderItem;

@Mapper(config = MapperConfig.class)
public interface OrderItemMapper {
    @Mapping(source = "book.id", target = "bookId")
    OrderItemDto toDto(OrderItem orderItem);

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "book.price", target = "price")
    OrderItem fromCartItemToOrderItem(CartItem cartItem);
}
