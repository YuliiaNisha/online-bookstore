package project.bookstore.service.shoppingcart;

import project.bookstore.dto.shoppingcart.AddBookToCartRequestDto;
import project.bookstore.dto.shoppingcart.ShoppingCartDto;
import project.bookstore.dto.shoppingcart.UpdateBookQuantityRequestDto;
import project.bookstore.model.User;

public interface ShoppingCartService {
    void createShoppingCart(User user);

    ShoppingCartDto findCartByUserId(Long userId);

    ShoppingCartDto addBookToCart(AddBookToCartRequestDto requestDto, Long userId);

    ShoppingCartDto updateBookQuantity(
            UpdateBookQuantityRequestDto requestDto,
            Long cartItemId,
            Long userId);

    ShoppingCartDto deleteBookFromCart(Long userId, Long cartItemId);
}
