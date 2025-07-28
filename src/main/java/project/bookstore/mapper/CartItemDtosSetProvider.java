package project.bookstore.mapper;

import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;
import project.bookstore.dto.shoppingcart.CartItemDto;
import project.bookstore.model.CartItem;

@RequiredArgsConstructor
@Component
public class CartItemDtosSetProvider {
    private final CartItemMapper cartItemMapper;

    @Named("getCartItemDtosSet")
    public Set<CartItemDto> getCartItemDtosSet(Set<CartItem> cartItems) {
        return cartItems.stream()
                .map(cartItemMapper::toDto)
                .collect(Collectors.toSet());
    }
}
