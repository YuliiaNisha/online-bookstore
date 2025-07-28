package project.bookstore.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.bookstore.dto.shoppingcart.AddBookToCartRequestDto;
import project.bookstore.dto.shoppingcart.ShoppingCartDto;
import project.bookstore.dto.shoppingcart.UpdateBookQuantityRequestDto;
import project.bookstore.model.User;
import project.bookstore.service.shoppingcart.ShoppingCartService;

@Tag(name = "Shopping Cart",
        description = "Endpoints for managing shopping cart")
@RequiredArgsConstructor
@RequestMapping("/cart")
@RestController
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;

    @Operation(summary = "Get Shopping Cart",
            description = "Provides user's shopping cart",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "Successfully retrieved a shopping cart"),
            }
    )
    @GetMapping
    public ShoppingCartDto getShoppingCart(
            @AuthenticationPrincipal User user
    ) {
        return shoppingCartService.findCartByUserId(user.getId());
    }

    @Operation(summary = "Add book to shopping cart",
            description = "Adds a book to shopping cart",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "Successfully created a shopping cart"),
                    @ApiResponse(responseCode = "400",
                            description = "Invalid input")
            }
    )
    @PostMapping
    public ShoppingCartDto addBookToCart(
            @RequestBody @Valid AddBookToCartRequestDto requestDto,
            @AuthenticationPrincipal User user) {
        return shoppingCartService.addBookToCart(requestDto, user.getId());
    }

    @Operation(summary = "Update book quantity",
            description = "Updates book quantity in a shopping cart",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "Successfully updated quantity"),
                    @ApiResponse(responseCode = "400",
                            description = "Invalid ID supplied"),
                    @ApiResponse(responseCode = "404",
                            description = "Shopping cart not found")
            }
    )
    @PutMapping("/items/{cartItemId}")
    public ShoppingCartDto updateBookQuantity(
            @RequestBody @Valid UpdateBookQuantityRequestDto requestDto,
            @PathVariable Long cartItemId,
            @AuthenticationPrincipal User user) {
        return shoppingCartService.updateBookQuantity(requestDto, cartItemId, user.getId());
    }

    @Operation(summary = "Delete a book",
            description = "Deletes book from shopping cart",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "Successfully deleted the book"),
                    @ApiResponse(responseCode = "400",
                            description = "Invalid ID supplied"),
                    @ApiResponse(responseCode = "404",
                            description = "Cart item not found")
            }
    )
    @DeleteMapping("/items/{cartItemId}")
    public ShoppingCartDto deleteBookFromCart(
            @AuthenticationPrincipal User user,
            @PathVariable Long cartItemId
    ) {
        return shoppingCartService.deleteBookFromCart(user.getId(), cartItemId);
    }
}
