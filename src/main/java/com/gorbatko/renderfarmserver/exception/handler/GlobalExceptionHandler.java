package com.gorbatko.renderfarmserver.exception.handler;

import com.gorbatko.renderfarmserver.dto.ExceptionResponseDto;
import com.gorbatko.renderfarmserver.exception.RenderFarmException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RenderFarmException.class)
    public ResponseEntity<ExceptionResponseDto> handleRenderFarmException(final RenderFarmException exception) {
        return ResponseEntity.status(exception.getStatus())
                .body(new ExceptionResponseDto(exception.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponseDto> handleException(final Exception exception) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ExceptionResponseDto(exception.getMessage()));
    }

}
