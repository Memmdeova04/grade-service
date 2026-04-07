package org.example.gradeservice.exception;

public class GradeNotFoundException extends RuntimeException {
    public GradeNotFoundException(String message) {
        super(message);
    }
}
