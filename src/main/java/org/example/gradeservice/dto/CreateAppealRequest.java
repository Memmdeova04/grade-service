package org.example.gradeservice.dto;


import lombok.Data;

@Data
public class CreateAppealRequest {
    private Long studentId;
    private Long gradeId;
    private String message;
}