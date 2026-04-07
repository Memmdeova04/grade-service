package org.example.gradeservice.dto;


import lombok.Data;
import org.example.gradeservice.util.GradeStatus;

@Data
public class GradeResponse {
    private Long id;
    private Long studentId;
    private String studentName;
    private Long subjectId;
    private String subjectName;
    private Double activity;
    private Double midterm;
    private Double presentation;
    private Double independentWork;
    private Double semesterScore;
    private Double examScore;
    private Double finalScore;
    private GradeStatus status;
    private String letterGrade;
}