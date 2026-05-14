package org.example.gradeservice.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.example.gradeservice.util.AppealStatus;
import java.time.LocalDateTime;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AppealResponse {
    Long id;
    Long studentId;
    Long gradeId;
    String message;
    AppealStatus status;
    LocalDateTime createdAt;
    String studentName;
}