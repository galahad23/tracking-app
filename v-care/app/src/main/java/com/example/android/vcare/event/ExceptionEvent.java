package com.example.android.vcare.event;

public class ExceptionEvent {

    private String errorMessage;
    private int hashCode;

    public ExceptionEvent(String errorMessage, int hashCode) {
        this.errorMessage = errorMessage;
        this.hashCode = hashCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public int getHashCode() {
        return hashCode;
    }
}
