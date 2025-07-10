package project.bookstore.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.bookstore.dto.user.UserLoginRequestDto;
import project.bookstore.dto.user.UserLoginResponseDto;
import project.bookstore.dto.user.UserRegistrationRequestDto;
import project.bookstore.dto.user.UserResponseDto;
import project.bookstore.exception.RegistrationException;
import project.bookstore.security.AuthenticationService;
import project.bookstore.service.user.UserService;

@Tag(name = "Authentication API",
        description = "Endpoints for user authentication")
@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthenticationController {
    private final UserService userService;
    private final AuthenticationService authenticationService;

    @Operation(summary = "Register a new user",
            description = "Adds a new user to DB",
            parameters = {
                    @Parameter(name = "email",
                            description = "User email"),
                    @Parameter(name = "password",
                            description = "User password"),
                    @Parameter(name = "repeatPassword",
                            description = "User password repeated"),
                    @Parameter(name = "firstName",
                            description = "User first name"),
                    @Parameter(name = "lastName",
                            description = "User last name"),
                    @Parameter(name = "shippingAddress",
                            description = "User shipping address")
            },
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "Successfully added a new user to DB"),
                    @ApiResponse(responseCode = "400",
                            description = "User with this email is already registered "
                                   + "or input data is invalid")
            }
    )
    @PostMapping("/registration")
    UserResponseDto registerUser(
            @RequestBody @Valid UserRegistrationRequestDto requestDto
    ) throws RegistrationException {
        return userService.register(requestDto);
    }

    @Operation(summary = "User login",
            description = "Allows user to log in",
            parameters = {
                    @Parameter(name = "email",
                            description = "User email"),
                    @Parameter(name = "password",
                            description = "User password")
            },
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "User successfully logged in "),
                    @ApiResponse(responseCode = "400",
                            description = "Invalid request"),
                    @ApiResponse(responseCode = "401",
                            description = "Invalid credentials")
            }
    )
    @PostMapping("/login")
    UserLoginResponseDto login(@RequestBody @Valid UserLoginRequestDto requestDto) {
        return authenticationService.authenticate(requestDto);
    }
}
