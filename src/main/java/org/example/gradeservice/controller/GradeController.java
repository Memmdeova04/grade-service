package org.example.gradeservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.gradeservice.Service.GradingService;
import org.example.gradeservice.dto.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/grades")
@RequiredArgsConstructor
public class GradeController {

    private final GradingService gradingService;

    @PutMapping("/activity")
    public ResponseEntity<GradeResponse> updateActivity(
            @Valid @RequestBody ActivityScoreRequest request) {
        return ResponseEntity.ok(gradingService.updateActivity(request));
    }

    @PutMapping("/midterm")
    public ResponseEntity<GradeResponse> updateMidterm(
            @Valid @RequestBody MidtermScoreRequest request) {
        return ResponseEntity.ok(gradingService.updateMidterm(request));
    }

    @PutMapping("/presentation")
    public ResponseEntity<GradeResponse> updatePresentation(
            @Valid @RequestBody PresentationScoreRequest request) {
        return ResponseEntity.ok(gradingService.updatePresentation(request));
    }
    @PostMapping("/initialize")
    public ResponseEntity<List<GradeResponse>> initializeGrades(
            @RequestParam Long subjectId,
            @RequestParam Long groupId) {
        return ResponseEntity.ok(gradingService.initializeGradesForGroup(subjectId, groupId));
    }


    @PutMapping("/independent-work")
    public ResponseEntity<GradeResponse> updateIndependentWork(
            @Valid @RequestBody IndependentWorkScoreRequest request) {
        return ResponseEntity.ok(gradingService.updateIndependentWork(request));
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<GradeResponse>> getGradesByStudent(
            @PathVariable Long studentId) {
        return ResponseEntity.ok(gradingService.getGradesByStudent(studentId));
    }

    @GetMapping("/student/{studentId}/subject/{subjectId}")
    public ResponseEntity<GradeResponse> getGradeByStudentAndSubject(
            @PathVariable Long studentId,
            @PathVariable Long subjectId) {
        return ResponseEntity.ok(gradingService.getGradeByStudentAndSubject(studentId, subjectId));
    }

    @GetMapping("/subject/{subjectId}")
    public ResponseEntity<List<GradeResponse>> getGradesBySubject(
            @PathVariable Long subjectId) {
        return ResponseEntity.ok(gradingService.getGradesBySubject(subjectId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<GradeResponse> getGrade(@PathVariable Long id) {
        return ResponseEntity.ok(gradingService.getGrade(id));
    }
}