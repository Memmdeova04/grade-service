package org.example.gradeservice.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.gradeservice.client.AcademicServiceClient;
import org.example.gradeservice.client.AuthServiceClient;
import org.example.gradeservice.dto.SubjectDto;
import org.example.gradeservice.entity.Grade;
import org.example.gradeservice.repository.GradeRepository;
import org.example.gradeservice.util.GradeStatus;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class ExamResultConsumer {

    private final GradeRepository gradeRepository;
    private final AcademicServiceClient academicServiceClient;
    private final AuthServiceClient authServiceClient;

    @Transactional
    @KafkaListener(topics = "exam-results", groupId = "grade-group")
    public void handleExamResult(Map<String, Object> payload) {
        try {
            Long studentId = Long.valueOf(payload.get("studentId").toString());
            Long subjectId = Long.valueOf(payload.get("subjectId").toString());
            Double examScore = Double.valueOf(payload.get("examScore").toString());

            Grade grade = gradeRepository
                    .findByStudentIdAndSubjectId(studentId, subjectId)
                    .orElseGet(() -> {
                        Grade g = new Grade();
                        g.setStudentId(studentId);
                        g.setSubjectId(subjectId);
                        return g;
                    });

            grade.setExamScore(examScore);
            gradeRepository.save(grade);

            recalculateGpa(studentId);

            log.info("İmtahan balı qiymətlərə yazıldı - studentId: {}, bal: {}",
                    studentId, examScore);
        } catch (Exception e) {
            log.error("İmtahan nəticəsi yazılarkən xəta: {}", e.getMessage());
        }
    }

    private void recalculateGpa(Long studentId) {
        List<Grade> grades = gradeRepository.findAllByStudentId(studentId);

        double totalWeighted = 0.0;
        int totalCredits = 0;

        for (Grade g : grades) {
            if (g.getFinalScore() == null) continue;
            try {
                SubjectDto subject = academicServiceClient.getSubjectById(g.getSubjectId());
                int credits = subject.getCredits();
                totalWeighted += g.getFinalScore() * credits;
                totalCredits += credits;
            } catch (Exception e) {
                log.warn("GPA hesablanarkən subject alına bilmədi: {}", e.getMessage());
            }
        }

        double gpa = totalCredits > 0
                ? Math.round((totalWeighted / totalCredits) * 100.0) / 100.0
                : 0.0;

        authServiceClient.updateStudentGpa(studentId, gpa);
        log.info("GPA yeniləndi - studentId: {}, gpa: {}", studentId, gpa);
    }


}