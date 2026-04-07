package org.example.gradeservice.dto;



import lombok.Data;

@Data
public class CreateGradeRequest {
    private Long studentId;
    private Long subjectId;
    private Double activity;
    private Double independentWork;
    private Double presentation;
    private Double colloquium;
    private Double examScore;
}
