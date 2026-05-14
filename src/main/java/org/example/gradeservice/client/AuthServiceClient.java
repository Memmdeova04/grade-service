package org.example.gradeservice.client;

import org.example.gradeservice.dto.StudentDto;
import org.example.gradeservice.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "auth-service", url = "${auth-service.url}")
public interface AuthServiceClient {
    @GetMapping("/api/auth/users/{id}")
    UserDto getUserById(@PathVariable Long id);

    @GetMapping("/api/auth/students/{id}")
    StudentDto getStudentById(@PathVariable Long id);


    @PutMapping("/api/auth/students/{id}/gpa")
    void updateStudentGpa(@PathVariable Long id, @RequestParam Double gpa);
    @GetMapping("/api/auth/students/group/{groupId}")
    java.util.List<StudentDto> getStudentsByGroup(@PathVariable("groupId") Long groupId);
}


