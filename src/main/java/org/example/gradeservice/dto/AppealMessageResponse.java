package org.example.gradeservice.dto;



import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AppealMessageResponse {
    Long id;
    Long appealId;
    Long senderId;
    String message;
    String senderRole;
    LocalDateTime createdAt;
}
