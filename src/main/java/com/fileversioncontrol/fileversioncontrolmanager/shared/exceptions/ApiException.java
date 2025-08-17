package com.fileversioncontrol.fileversioncontrolmanager.shared.exceptions;

import org.springframework.http.HttpStatus;

import java.util.List;

public class ApiException extends RuntimeException {
    private final HttpStatus status;
    private final List<String> results;

    public ApiException(HttpStatus status, List<String> results, String message) {
        super(message);
        this.status = status;
        this.results = results;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public List<String> getResults() {
        return results;
    }
}
