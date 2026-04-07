package org.example.gradeservice.exception;

public class SubjectNotFoundException extends RuntimeException {
    public SubjectNotFoundException(String message) {
        super(message);
    }
}