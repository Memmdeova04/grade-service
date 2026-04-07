package org.example.gradeservice.mapper;

import org.example.gradeservice.dto.GradeResponse;
import org.example.gradeservice.entity.Grade;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface GradeMapper {

    @Mapping(target = "studentName",   ignore = true)
    @Mapping(target = "subjectName",   ignore = true)
    @Mapping(target = "semesterScore", expression = "java(grade.getSemesterScore())")
    @Mapping(target = "finalScore",    expression = "java(grade.getFinalScore())")
    @Mapping(target = "status",        expression = "java(grade.getStatus())")
    @Mapping(target = "letterGrade",   expression = "java(grade.getLetterGrade())")
    GradeResponse toResponse(Grade grade);
}