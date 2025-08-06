package project.bookstore.dto.order;

import jakarta.validation.constraints.NotBlank;
import project.bookstore.model.Order;
import project.bookstore.validation.ValidEnumFieldValue;

@ValidEnumFieldValue(
        fieldToValidate = "status",
        enumClass = Order.Status.class,
        message = "Invalid status value. "
)
public record UpdateOrderStatusRequestDto(
        @NotBlank
        String status
) {
}
