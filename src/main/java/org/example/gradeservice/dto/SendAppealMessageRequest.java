package org.example.gradeservice.dto;


import lombok.Data;

@Data
public class SendAppealMessageRequest {
    private Long senderId;
    private String message;
    private boolean teacher;
}