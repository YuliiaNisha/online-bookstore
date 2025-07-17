package project.bookstore.service.user;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import project.bookstore.dto.user.UserRegistrationRequestDto;
import project.bookstore.dto.user.UserResponseDto;
import project.bookstore.exception.EntityNotFoundException;
import project.bookstore.exception.RegistrationException;
import project.bookstore.mapper.UserMapper;
import project.bookstore.model.Role;
import project.bookstore.model.User;
import project.bookstore.repository.RoleRepository;
import project.bookstore.repository.UserRepository;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    public static final long USER_ROLE_ID = 1L;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private Role roleUser;

    @PostConstruct
    private void init() {
        roleUser = roleRepository.findByRole(Role.RoleName.ROLE_USER)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Can't find role by id: " + USER_ROLE_ID)
        );
    }

    @Transactional
    @Override
    public UserResponseDto register(
            UserRegistrationRequestDto requestDto
    ) throws RegistrationException {
        if (userRepository.existsByEmail(requestDto.email())) {
            throw new RegistrationException("Can't register user. User with email: "
            + requestDto.email() + " is already registered.");
        }
        User user = userMapper.toModel(requestDto);
        user.setPassword(passwordEncoder.encode(requestDto.password()));
        user.getRoles().add(roleUser);
        return userMapper.toDto(userRepository.save(user));
    }
}
