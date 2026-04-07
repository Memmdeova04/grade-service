package org.example.gradeservice.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.gradeservice.repository.AppealRepository;
import org.example.gradeservice.repository.AppealMessageRepository;
import org.example.gradeservice.repository.GradeRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class StudentDeletedConsumer {

    private final GradeRepository gradeRepository;
    private final AppealRepository appealRepository;
    private final AppealMessageRepository appealMessageRepository;

    @Transactional
    @KafkaListener(topics = "student-deleted", groupId = "grade-group")
    public void handleStudentDeleted(Map<String, Object> payload) {
        try {
            Long studentId = Long.valueOf(payload.get("studentId").toString());


            List<Long> appealIds = appealRepository.findByStudentId(studentId)
                    .stream()
                    .map(a -> a.getId())
                    .toList();

            appealMessageRepository.deleteByAppealIdIn(appealIds);
            appealRepository.deleteByStudentId(studentId);
            gradeRepository.deleteByStudentId(studentId);

            log.info("Tələbəyə aid qiymət və apellyasiya silindi - studentId: {}", studentId);
        } catch (Exception e) {
            log.error("Grade/Appeal silinərkən xəta: {}", e.getMessage());
        }
    }
}
