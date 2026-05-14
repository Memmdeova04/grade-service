package org.example.gradeservice.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class IndependentWorkScoreRequest {
    @NotNull
    Long studentId;
    @NotNull
    Long subjectId;
    @NotNull
    @Min(0) @Max(10)
    Double independentWork;
}