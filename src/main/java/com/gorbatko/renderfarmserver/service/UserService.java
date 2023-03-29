package com.gorbatko.renderfarmserver.service;

import com.gorbatko.renderfarmserver.dto.UserServiceResponseDto;
import com.gorbatko.renderfarmserver.dto.UserDto;
import com.gorbatko.renderfarmserver.entity.User;
import com.gorbatko.renderfarmserver.exception.RenderFarmException;
import com.gorbatko.renderfarmserver.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import static com.gorbatko.renderfarmserver.exception.RenderFarmException.INCORRECT_PASSWORD;
import static com.gorbatko.renderfarmserver.exception.RenderFarmException.USER_ALREADY_EXISTS;
import static com.gorbatko.renderfarmserver.exception.RenderFarmException.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private static final String USER_REGISTERED = "User has been registered!";
    private static final String USER_LOGIN = "Your login was successful!";

    public UserServiceResponseDto registerUser(UserDto dto) {
        User user = new User();
        if (userRepository.existsByLogin(dto.getLogin())) {
            throw new RenderFarmException(HttpStatus.BAD_REQUEST, USER_ALREADY_EXISTS);
        }
        user.setLogin(dto.getLogin());
        user.setPassword(encryptPassword(dto.getPassword()));
        userRepository.save(user);
        return new UserServiceResponseDto(USER_REGISTERED, user.getId());
    }

    public UserServiceResponseDto loginUser(UserDto dto) {
        if (!userRepository.existsByLogin(dto.getLogin())) {
            throw new RenderFarmException(HttpStatus.BAD_REQUEST, USER_NOT_FOUND);
        }
        User user = userRepository.findByLogin(dto.getLogin());
        if (!BCrypt.checkpw(dto.getPassword(), user.getPassword())) {
            throw new RenderFarmException(HttpStatus.BAD_REQUEST, INCORRECT_PASSWORD);
        }
        return new UserServiceResponseDto(USER_LOGIN, user.getId());
    }

    private String encryptPassword(String password) {
        return new BCryptPasswordEncoder().encode(password);
    }


}
