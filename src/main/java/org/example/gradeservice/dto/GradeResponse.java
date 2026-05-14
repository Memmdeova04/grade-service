package org.example.gradeservice.dto;


import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.example.gradeservice.util.GradeStatus;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GradeResponse {
    Long id;
    Long studentId;
    String studentName;
    Long subjectId;
    String subjectName;
    Double activity;
    Double midterm;
    Double presentation;
    Double independentWork;
    Double semesterScore;
    Double examScore;
    Double finalScore;
    GradeStatus status;
    String letterGrade;
}