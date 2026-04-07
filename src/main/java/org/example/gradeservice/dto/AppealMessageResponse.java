package org.example.gradeservice.dto;



import lombok.Data;
import java.time.LocalDateTime;

@Data
public class AppealMessageResponse {
    private Long id;
    private Long appealId;
    private Long senderId;
    private String message;
    private String senderRole;
    private LocalDateTime createdAt;
}
