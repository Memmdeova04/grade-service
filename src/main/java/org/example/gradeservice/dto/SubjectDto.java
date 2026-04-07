package org.example.gradeservice.dto;


import lombok.Data;

@Data
public class SubjectDto {
    private Long id;
    private String name;
    private Integer credits;
}