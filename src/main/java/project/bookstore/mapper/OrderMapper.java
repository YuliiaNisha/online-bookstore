package project.bookstore.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import project.bookstore.config.MapperConfig;
import project.bookstore.dto.order.OrderDto;
import project.bookstore.model.Order;

@Mapper(config = MapperConfig.class, uses = OrderItemDtosSetProvider.class)
public interface OrderMapper {
    @Mapping(source = "user.firstName", target = "firstName")
    @Mapping(source = "user.lastName", target = "lastName")
    @Mapping(
            source = "orderItems",
            target = "orderItemDtos",
            qualifiedByName = "getOrderItemDtosSet")
    OrderDto toDto(Order order);
}
