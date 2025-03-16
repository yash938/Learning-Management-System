package com.LMS.Exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

        @ExceptionHandler(ResourceNotFoundException.class)
        public ResponseEntity<HandlerException> resourceNotFoundException(ResourceNotFoundException ex){
            log.info("Exception handler invoked {}",ex.getMessage());
            HandlerException resourceNotFound = HandlerException.builder().message("Resource not found").success(false).httpStatus(HttpStatus.NOT_FOUND).localDate(LocalDate.now()).build();
            return new ResponseEntity<>(resourceNotFound,HttpStatus.NOT_FOUND);
        }

        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<Map<String, String>> handleApiResponseException(MethodArgumentNotValidException ex) {
            Map<String, String> errors = new HashMap<>();

            ex.getBindingResult().getAllErrors().forEach((error) -> {
                String fieldName = ((FieldError) error).getField();  // Get the field name
                String errorMessage = error.getDefaultMessage();     // Get the default message from the validation
                errors.put(fieldName, errorMessage);                 // Add to the errors map
            });

            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }

}
