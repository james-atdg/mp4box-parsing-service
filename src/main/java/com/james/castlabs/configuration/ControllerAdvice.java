package com.james.castlabs.configuration;

import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;
import java.net.MalformedURLException;


import com.james.castlabs.exception.ErrorMessage;
import com.james.castlabs.exception.ErrorType;
import com.james.castlabs.exception.ServiceException;
import com.james.castlabs.util.LocaleMessageSource;

import static java.util.Optional.ofNullable;
import jakarta.validation.ConstraintViolationException;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class ControllerAdvice {

    private final LocaleMessageSource messages;

    @ExceptionHandler({ServiceException.class})
    public ResponseEntity<ErrorMessage> handleServiceException(ServiceException e) {
        log.error(e.getMessage());
        String message = messages.getMessage(e.getMessage(), e.getParams());
        ErrorMessage errorMessage = new ErrorMessage()
            .setType(e.getType())
            .setMessage(message)
            .setDateTime(LocalDateTime.now());
        return toResponseEntity(errorMessage);
    }
    
    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<ErrorMessage> handleConstraintException(ConstraintViolationException e) {
        log.error(e.getMessage());
        String message = messages.getMessage(e.getMessage());
        ErrorMessage errorMessage = new ErrorMessage()
            .setType(ErrorType.BAD_REQUEST)
            .setMessage(message)
            .setDateTime(LocalDateTime.now());
        return toResponseEntity(errorMessage);
    }
    
    @ExceptionHandler({IOException.class})
    public ResponseEntity<ErrorMessage> handleIOException(IOException e) {
        log.error(e.getMessage());
        String message = messages.getMessage(e.getMessage());
        ErrorMessage errorMessage = new ErrorMessage()
            .setType(ErrorType.BAD_REQUEST)
            .setMessage(message)
            .setDateTime(LocalDateTime.now());
        return toResponseEntity(errorMessage);
    }
    
    @ExceptionHandler({MalformedURLException.class})
    public ResponseEntity<ErrorMessage> handleMalformedURLException(MalformedURLException e) {
        log.error(e.getMessage());
        String message = messages.getMessage(e.getMessage());
        ErrorMessage errorMessage = new ErrorMessage()
            .setType(ErrorType.BAD_REQUEST)
            .setMessage(message)
            .setDateTime(LocalDateTime.now());
        return toResponseEntity(errorMessage);
    }
    
    @ExceptionHandler({InterruptedException.class})
    public ResponseEntity<ErrorMessage> handleInterruptedException(InterruptedException e) {
        log.error(e.getMessage());
        String message = messages.getMessage(e.getMessage());
        ErrorMessage errorMessage = new ErrorMessage()
            .setType(ErrorType.INTERNAL_SERVER_ERROR)
            .setMessage(message)
            .setDateTime(LocalDateTime.now());
        return toResponseEntity(errorMessage);
    }

    private ResponseEntity<ErrorMessage> toResponseEntity(ErrorMessage errorMessage) {
        int status = ofNullable(errorMessage.getType())
            .map(ErrorType::getStatus)
            .orElse(HttpStatus.INTERNAL_SERVER_ERROR.value());
        return ResponseEntity.status(status).body(errorMessage);
    }

}
