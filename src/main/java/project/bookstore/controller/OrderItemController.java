package project.bookstore.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.bookstore.dto.order.OrderItemDto;
import project.bookstore.model.User;
import project.bookstore.service.orderitem.OrderItemService;

@Tag(name = "Order Items",
        description = "Endpoints for managing order items")
@RequiredArgsConstructor
@RequestMapping("/orders")
@RestController
public class OrderItemController {
    private final OrderItemService orderItemService;

    @Operation(summary = "Get order items by order id",
            description = "Provides order items by corresponding order id",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "Successfully found order items"),
                    @ApiResponse(responseCode = "400",
                            description = "Invalid ID supplied"),
                    @ApiResponse(responseCode = "404",
                            description = "Order not found")
            }
    )
    @GetMapping("/{orderId}/items")
    public Set<OrderItemDto> getOrderItemsByOrderId(
            @PathVariable Long orderId,
            @AuthenticationPrincipal User user
    ) {
        return orderItemService.findOrderItemsByOrderId(orderId, user.getId());
    }

    @Operation(summary = "Get order item id",
            description = "Provides order item by its id",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "Successfully found order item"),
                    @ApiResponse(responseCode = "400",
                            description = "Invalid ID supplied"),
                    @ApiResponse(responseCode = "404",
                            description = "Order item not found")
            }
    )
    @GetMapping("/{orderId}/items/{id}")
    public OrderItemDto getOrderItemById(
            @PathVariable Long orderId,
            @PathVariable Long id,
            @AuthenticationPrincipal User user
    ) {
        return orderItemService.findOrderItemById(orderId, id, user.getId());
    }
}
