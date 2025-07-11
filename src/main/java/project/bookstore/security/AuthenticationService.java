package project.bookstore.security;

import project.bookstore.dto.user.UserLoginRequestDto;
import project.bookstore.dto.user.UserLoginResponseDto;

public interface AuthenticationService {
    UserLoginResponseDto authenticate(UserLoginRequestDto requestDto);
}
