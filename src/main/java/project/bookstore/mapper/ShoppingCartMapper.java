package project.bookstore.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import project.bookstore.config.MapperConfig;
import project.bookstore.dto.shoppingcart.ShoppingCartDto;
import project.bookstore.model.ShoppingCart;

@Mapper(config = MapperConfig.class, uses = CartItemDtosSetProvider.class)
public interface ShoppingCartMapper {
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "cartItems", target = "cartItemDtos", qualifiedByName = "getCartItemDtosSet")
    ShoppingCartDto toDto(ShoppingCart shoppingCart);
}
