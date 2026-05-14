package org.example.gradeservice.dto;


import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SendAppealMessageRequest {
    Long senderId;
    String message;
    boolean teacher;
}