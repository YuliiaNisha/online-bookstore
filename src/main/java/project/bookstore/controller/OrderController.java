package project.bookstore.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import project.bookstore.dto.order.CreateOrderRequestDto;
import project.bookstore.dto.order.OrderDto;
import project.bookstore.dto.order.UpdateOrderStatusRequestDto;
import project.bookstore.model.User;
import project.bookstore.service.order.OrderService;

@Tag(name = "Orders",
        description = "Endpoints for managing orders")
@RequiredArgsConstructor
@RequestMapping("/orders")
@RestController
public class OrderController {
    private final OrderService orderService;

    @Operation(summary = "Create a new order",
            description = "Adds a new order to DB",
            responses = {
                    @ApiResponse(responseCode = "201",
                            description = "Successfully created a new order"),
                    @ApiResponse(responseCode = "400",
                            description = "Invalid input or shopping cart is empty")
            }
    )
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public OrderDto createOrder(
            @RequestBody CreateOrderRequestDto requestDto,
            @AuthenticationPrincipal User user) {
        return orderService.createOrder(requestDto, user);
    }

    @Operation(summary = "Get all orders for an authorised user",
            description = "Provides a list of all orders. "
                    + "Supports pagination and sorting",
            parameters = {
                    @Parameter(name = "page",
                            description = "Number of a page to provide"),
                    @Parameter(name = "size",
                            description = "Number of orders per page"),
                    @Parameter(name = "sort",
                            description = "Sorting criteria for the output")
            },
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "Successfully retrieved all orders")
            }
    )
    @GetMapping
    public Page<OrderDto> getAllOrders(
            @AuthenticationPrincipal User user,
            Pageable pageable
    ) {
        return orderService.findAllOrders(user.getId(), pageable);
    }

    @Operation(summary = "Update an order",
            description = "Updates info about an order",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "Successfully updated the order status"),
                    @ApiResponse(responseCode = "400",
                            description = "Invalid ID supplied"),
                    @ApiResponse(responseCode = "404",
                            description = "Order not found")
            }
    )
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}")
    public OrderDto updateStatus(
            @PathVariable Long id,
            @RequestBody @Valid UpdateOrderStatusRequestDto requestDto
    ) {
        return orderService.updateStatus(id, requestDto);
    }
}
