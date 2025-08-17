package com.fileversioncontrol.fileversioncontrolmanager.shared.advice;

import com.fileversioncontrol.fileversioncontrolmanager.shared.dto.ErrorResponse;
import com.fileversioncontrol.fileversioncontrolmanager.shared.exceptions.ApiException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ErrorResponse> handleApiException(ApiException ex) {
        ErrorResponse error = new ErrorResponse(
                ex.getStatus().value(),
                ex.getResults(),
                ex.getMessage()
        );
        return ResponseEntity.status(ex.getStatus()).body(error);
    }
}
