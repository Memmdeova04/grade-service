package org.example.gradeservice.dto;



import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateGradeRequest {
    Long studentId;
    Long subjectId;
    Double activity;
    Double independentWork;
    Double presentation;
    Double colloquium;
    Double examScore;
}
