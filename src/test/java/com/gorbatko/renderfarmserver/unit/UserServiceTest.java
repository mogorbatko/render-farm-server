package com.gorbatko.renderfarmserver.unit;

import com.gorbatko.renderfarmserver.dto.UserServiceResponseDto;
import com.gorbatko.renderfarmserver.dto.UserDto;
import com.gorbatko.renderfarmserver.entity.User;
import com.gorbatko.renderfarmserver.exception.RenderFarmException;
import com.gorbatko.renderfarmserver.repository.UserRepository;
import com.gorbatko.renderfarmserver.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    private static final long USER_ID = 0;
    private static final String LOGIN = "user";
    private static final String PASSWORD = "password";
    private static final String INCORRECT_PASSWORD = "password1";
    private static final String USER_REGISTERED = "User has been registered!";
    private static final String USER_LOGIN = "Your login was successful!";

    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserService userService;
    private UserDto dto;
    private User user;

    @BeforeEach
    void setUp() {
        dto = new UserDto()
                .setLogin(LOGIN)
                .setPassword(PASSWORD);

        user = new User()
                .setId(USER_ID)
                .setLogin(LOGIN)
                .setPassword(new BCryptPasswordEncoder().encode(PASSWORD));
    }

    @Test
    public void shouldSuccessRegisterUser() {
        UserServiceResponseDto response = userService.registerUser(dto);
        verify(userRepository, times(1)).existsByLogin(LOGIN);
        verify(userRepository, times(1)).save(ArgumentMatchers.any(User.class));
        assertEquals(response, new UserServiceResponseDto(USER_REGISTERED, USER_ID));
    }

    @Test
    public void shouldFailedRegisterUserIfUserExists() {
        when(userRepository.existsByLogin(dto.getLogin())).thenReturn(true);
        RenderFarmException exception = assertThrows(RenderFarmException.class, () -> userService.registerUser(dto));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        verify(userRepository, times(1)).existsByLogin(LOGIN);
        verify(userRepository, times(0)).save(ArgumentMatchers.any(User.class));
    }

    @Test
    public void shouldSuccessLoginUser() {
        when(userRepository.existsByLogin(dto.getLogin())).thenReturn(true);
        when(userRepository.findByLogin(dto.getLogin())).thenReturn(user);
        UserServiceResponseDto response = userService.loginUser(dto);
        verify(userRepository, times(1)).existsByLogin(LOGIN);
        verify(userRepository, times(1)).findByLogin(LOGIN);
        assertEquals(response, new UserServiceResponseDto(USER_LOGIN, USER_ID));
    }

    @Test
    public void shouldFailedLoginUserIfUserNotExist() {
        when(userRepository.existsByLogin(dto.getLogin())).thenReturn(false);
        RenderFarmException exception = assertThrows(RenderFarmException.class, () -> userService.loginUser(dto));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        verify(userRepository, times(1)).existsByLogin(LOGIN);
        verify(userRepository, times(0)).findByLogin(LOGIN);
    }

    @Test
    public void shouldFailedLoginUserIfPasswordIncorrect() {
        dto.setPassword(INCORRECT_PASSWORD);
        when(userRepository.existsByLogin(dto.getLogin())).thenReturn(true);
        when(userRepository.findByLogin(dto.getLogin())).thenReturn(user);
        RenderFarmException exception = assertThrows(RenderFarmException.class, () -> userService.loginUser(dto));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        verify(userRepository, times(1)).existsByLogin(LOGIN);
        verify(userRepository, times(1)).findByLogin(LOGIN);
    }


}
