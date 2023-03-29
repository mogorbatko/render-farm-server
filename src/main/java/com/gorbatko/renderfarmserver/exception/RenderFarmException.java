package com.gorbatko.renderfarmserver.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
@Getter
@Setter
public class RenderFarmException extends RuntimeException {
    public static final String USER_ALREADY_EXISTS = "User already exists.";
    public static final String INCORRECT_PASSWORD = "Password is incorrect.";
    public static final String USER_NOT_FOUND = "User not found.";
    public static final String TASK_NOT_FOUND = "Task not found.";
    private final HttpStatus status;

    public RenderFarmException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }
}
