package org.example.gradeservice.client;


import org.example.gradeservice.dto.SubjectDto;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "academic-service", url = "${academic-service.url}")
public interface AcademicServiceClient {
    @GetMapping("/api/academic/subjects/{id}")
    SubjectDto getSubjectById(@PathVariable Long id);
}