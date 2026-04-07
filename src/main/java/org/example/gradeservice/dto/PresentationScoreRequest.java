package org.example.gradeservice.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PresentationScoreRequest {
    @NotNull
    private Long studentId;
    @NotNull private Long subjectId;
    @NotNull @Min(0) @Max(10) private Double presentation;
}
