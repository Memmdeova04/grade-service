package org.example.gradeservice.dto;

import lombok.Data;

@Data
public class StudentDto {
    private Long id;
    private String firstName;
    private String lastName;
    private Long userId;
    private String email;
}