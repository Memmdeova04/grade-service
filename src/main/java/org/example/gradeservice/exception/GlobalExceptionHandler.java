package org.example.gradeservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(GradeNotFoundException.class)
    public ResponseEntity<String> handleGradeNotFound(GradeNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(AppealNotFoundException.class)
    public ResponseEntity<String> handleAppealNotFound(AppealNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(AppealAlreadyClosedException.class)
    public ResponseEntity<String> handleAppealAlreadyClosed(AppealAlreadyClosedException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(StudentNotFoundException.class)
    public ResponseEntity<String> handleStudentNotFound(StudentNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(SubjectNotFoundException.class)
    public ResponseEntity<String> handleSubjectNotFound(SubjectNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleGeneric(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Xəta baş verdi: " + ex.getMessage());
    }
     @ExceptionHandler(InvalidOperationException.class)
 public ResponseEntity<String> handleInvalidOperation(InvalidOperationException ex) {
     return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
 }
}
