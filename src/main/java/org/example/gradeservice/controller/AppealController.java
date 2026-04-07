package org.example.gradeservice.controller;



import lombok.RequiredArgsConstructor;
import org.example.gradeservice.Service.AppealService;
import org.example.gradeservice.dto.AppealMessageResponse;
import org.example.gradeservice.dto.AppealResponse;
import org.example.gradeservice.dto.CreateAppealRequest;
import org.example.gradeservice.dto.SendAppealMessageRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/appeals")
@RequiredArgsConstructor
public class AppealController {

    private final AppealService appealService;

    @PostMapping
    public ResponseEntity<AppealResponse> createAppeal(@RequestBody CreateAppealRequest request) {
        return ResponseEntity.ok(appealService.createAppeal(request));
    }

    @PostMapping("/{appealId}/messages")
    public ResponseEntity<AppealMessageResponse> sendMessage(
            @PathVariable Long appealId,
            @RequestBody SendAppealMessageRequest request) {
        return ResponseEntity.ok(appealService.sendMessage(appealId, request));
    }

    @PutMapping("/{appealId}/close")
    public ResponseEntity<Void> closeAppeal(@PathVariable Long appealId) {
        appealService.closeAppeal(appealId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{appealId}")
    public ResponseEntity<AppealResponse> getAppeal(@PathVariable Long appealId) {
        return ResponseEntity.ok(appealService.getAppeal(appealId));
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<AppealResponse>> getAppealsByStudent(@PathVariable Long studentId) {
        return ResponseEntity.ok(appealService.getAppealsByStudent(studentId));
    }

    @GetMapping("/grade/{gradeId}")
    public ResponseEntity<List<AppealResponse>> getAppealsByGrade(@PathVariable Long gradeId) {
        return ResponseEntity.ok(appealService.getAppealsByGrade(gradeId));
    }

    @GetMapping("/{appealId}/messages")
    public ResponseEntity<List<AppealMessageResponse>> getMessages(@PathVariable Long appealId) {
        return ResponseEntity.ok(appealService.getMessages(appealId));
    }

    @GetMapping("/open")
    public ResponseEntity<List<AppealResponse>> getOpenAppeals() {
        return ResponseEntity.ok(appealService.getOpenAppeals());
    }
}