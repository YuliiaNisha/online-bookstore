package project.bookstore.service.shoppingcart;

import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.bookstore.dto.shoppingcart.AddBookToCartRequestDto;
import project.bookstore.dto.shoppingcart.ShoppingCartDto;
import project.bookstore.dto.shoppingcart.UpdateBookQuantityRequestDto;
import project.bookstore.exception.EntityNotFoundException;
import project.bookstore.mapper.ShoppingCartMapper;
import project.bookstore.model.Book;
import project.bookstore.model.CartItem;
import project.bookstore.model.ShoppingCart;
import project.bookstore.model.User;
import project.bookstore.repository.BookRepository;
import project.bookstore.repository.ShoppingCartRepository;

@RequiredArgsConstructor
@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final ShoppingCartMapper shoppingCartMapper;
    private final BookRepository bookRepository;

    @Override
    public void createShoppingCart(User user) {
        if (!shoppingCartRepository.existsByUser(user)) {
            ShoppingCart shoppingCart = new ShoppingCart();
            shoppingCart.setUser(user);
            shoppingCartRepository.save(shoppingCart);
        }
    }

    @Override
    public ShoppingCartDto findCartByUserId(Long userId) {
        return shoppingCartMapper.toDto(findCart(userId));
    }

    @Override
    public ShoppingCartDto addBookToCart(AddBookToCartRequestDto requestDto, Long userId) {
        ShoppingCart shoppingCart = findCart(userId);
        Book bookToAdd = getBook(requestDto.bookId());
        Optional<CartItem> bookToAddCartItem = findCartItemByPredicate(
                shoppingCart, item -> item.getBook().equals(bookToAdd)
        );
        if (bookToAddCartItem.isPresent()) {
            CartItem cartItemToUpdate = bookToAddCartItem.get();
            cartItemToUpdate.setQuantity(cartItemToUpdate.getQuantity() + requestDto.quantity());
        } else {
            CartItem newCartItem = createNewCartItem(
                    shoppingCart, bookToAdd, requestDto.quantity()
            );
            shoppingCart.getCartItems().add(newCartItem);
        }
        ShoppingCart savedCart = shoppingCartRepository.save(shoppingCart);
        return shoppingCartMapper.toDto(savedCart);
    }

    @Override
    public ShoppingCartDto updateBookQuantity(
            UpdateBookQuantityRequestDto requestDto,
            Long cartItemId,
            Long userId
    ) {
        ShoppingCart shoppingCart = findCart(userId);
        CartItem cartItemToUpdate = findCartItemByPredicate(
                shoppingCart, item -> item.getId().equals(cartItemId)
        )
                .orElseThrow(() -> new EntityNotFoundException(
                        "Can't find cart item by id: " + cartItemId
                )
        );
        cartItemToUpdate.setQuantity(requestDto.quantity());
        return shoppingCartMapper.toDto(
                shoppingCartRepository.save(shoppingCart)
        );
    }

    @Override
    public ShoppingCartDto deleteBookFromCart(Long userId, Long cartItemId) {
        ShoppingCart shoppingCart = findCart(userId);
        Set<CartItem> cartItems = shoppingCart.getCartItems();
        boolean isRemoved = cartItems.removeIf(cartItem -> cartItem.getId().equals(cartItemId));
        if (!isRemoved) {
            throw new EntityNotFoundException("Can't find cart item by id: "
            + cartItemId + " in cart.");
        }
        return shoppingCartMapper.toDto(
                shoppingCartRepository.save(shoppingCart)
        );
    }

    private ShoppingCart findCart(Long userId) {
        return shoppingCartRepository.findByUserId(userId).orElseThrow(
                () -> new EntityNotFoundException(
                        "Can't find shopping cart by user id: " + userId
                )
        );
    }

    private CartItem createNewCartItem(
            ShoppingCart shoppingCart,
            Book bookToAdd,
            Integer quantity) {
        CartItem cartItem = new CartItem();
        cartItem.setShoppingCart(shoppingCart);
        cartItem.setBook(bookToAdd);
        cartItem.setQuantity(quantity);
        return cartItem;
    }

    private Optional<CartItem> findCartItemByPredicate(
            ShoppingCart shoppingCart,
            Predicate<CartItem> predicate
    ) {
        return shoppingCart.getCartItems()
                .stream()
                .filter(predicate)
                .findFirst();
    }

    private Book getBook(Long bookToAddId) {
        return bookRepository.findById(bookToAddId).orElseThrow(
                () -> new EntityNotFoundException(
                        "Can't find Book by id: " + bookToAddId)
        );
    }
}
