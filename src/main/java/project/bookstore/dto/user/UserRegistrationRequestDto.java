package project.bookstore.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import project.bookstore.validation.FieldMatch;

@FieldMatch(first = "password",
        second = "repeatPassword",
        message = "Password and repeat password must match")
public record UserRegistrationRequestDto(
        @Email(message = "Invalid format of email")
        @NotBlank(message = "Email is required. Please provide your email.")
        String email,

        @NotBlank(message = "Password is required. Please provide your password.")
        @Size(min = 8, max = 25, message = "Password must be between 8 and 25 digits")
        String password,

        @NotBlank(message = "Please, repeat your password.")
        @Size(min = 8, max = 25, message = "Repeat password must be between 8 and 25 digits")
        String repeatPassword,

        @NotBlank(message = "First name is required. Please provide your first name.")
        String firstName,

        @NotBlank(message = "Last name is required. Please provide your last name.")
        String lastName,

        @Size(max = 200, message = "Shipping address must not be longer than 200 digits")
        String shippingAddress
){
}
