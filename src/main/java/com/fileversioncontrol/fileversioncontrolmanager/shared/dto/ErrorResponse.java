package com.fileversioncontrol.fileversioncontrolmanager.shared.dto;

import java.util.List;

public class ErrorResponse {
    private int status;
    private List<String> results;
    private String message;

    public ErrorResponse(int status, List<String> results, String message) {
        this.status = status;
        this.results = results;
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<String> getResults() {
        return results;
    }

    public void setResults(List<String> results) {
        this.results = results;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
