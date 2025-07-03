package com.sas.sas_backend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ProblemDetail> handleBaseException(BaseException ex) {
        ProblemDetail pb = mapToProblemDetail(ex);
        return ResponseEntity.status(pb.getStatus()).body(pb);
    }

    ProblemDetail mapToProblemDetail(BaseException ex) {
        HttpStatus httpStatus = HttpStatus.resolve(Integer.parseInt(ex.getHttpStatusCode()));

        assert httpStatus != null;
        ProblemDetail pb = ProblemDetail.forStatus(httpStatus);
        pb.setTitle(ex.getTitle());
        pb.setDetail(ex.getDetail());
        return pb;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }
}
