package org.example.gradeservice.Service;

import lombok.RequiredArgsConstructor;
import org.example.gradeservice.client.AcademicServiceClient;
import org.example.gradeservice.client.AuthServiceClient;
import org.example.gradeservice.dto.StudentDto;
import org.example.gradeservice.dto.SubjectDto;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExternalDataService {

    private final AuthServiceClient authClient;
    private final AcademicServiceClient academicClient;

    @Cacheable(value = "external_students", key = "#studentId")
    public StudentDto getStudent(Long studentId) {
        return authClient.getStudentById(studentId);
    }

    @Cacheable(value = "external_subjects", key = "#subjectId")
    public SubjectDto getSubject(Long subjectId) {
        return academicClient.getSubjectById(subjectId);
    }
}