package org.example.gradeservice.exception;

public class AppealAlreadyClosedException extends RuntimeException {
    public AppealAlreadyClosedException(String message) {
        super(message);
    }
}
