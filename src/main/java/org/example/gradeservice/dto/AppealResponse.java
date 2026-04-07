package org.example.gradeservice.dto;

import lombok.Data;
import org.example.gradeservice.util.AppealStatus;
import java.time.LocalDateTime;

@Data
public class AppealResponse {
    private Long id;
    private Long studentId;
    private Long gradeId;
    private String message;
    private AppealStatus status;
    private LocalDateTime createdAt;
}