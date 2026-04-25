package com.Lifelink.HeathCareBridge.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgument.class)
    public ResponseEntity<String> handleIllegalArgument(IllegalArgument ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(DetailsNotFound.class)
    public ResponseEntity<String> handleDetailsNotFound(DetailsNotFound ex) {
        return ResponseEntity.status(404).body(ex.getMessage());
    }

    @ExceptionHandler(AlreadyExistsException.class)
    public ResponseEntity<String> handleAlreadyExists(AlreadyExistsException ex){
        String message = ex.getMessage();
        return new ResponseEntity<>(message , HttpStatus.CONFLICT);
    }
}
