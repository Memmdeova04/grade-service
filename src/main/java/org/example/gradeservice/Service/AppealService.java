package org.example.gradeservice.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.gradeservice.client.AuthServiceClient;
import org.example.gradeservice.dto.*;
import org.example.gradeservice.entity.Appeal;
import org.example.gradeservice.entity.AppealMessage;
import org.example.gradeservice.entity.Grade;
import org.example.gradeservice.exception.AppealAlreadyClosedException;
import org.example.gradeservice.exception.AppealNotFoundException;
import org.example.gradeservice.exception.GradeNotFoundException;
import org.example.gradeservice.mapper.AppealMapper;
import org.example.gradeservice.repository.AppealMessageRepository;
import org.example.gradeservice.repository.AppealRepository;
import org.example.gradeservice.repository.GradeRepository;
import org.example.gradeservice.util.AppealStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AppealService {

    private final AppealRepository appealRepository;
    private final AppealMessageRepository appealMessageRepository;
    private final GradeRepository gradeRepository;
    private final AuthServiceClient authServiceClient;
    private final AppealMapper appealMapper;

    @Transactional
    public AppealResponse createAppeal(CreateAppealRequest request) {
        Grade grade = gradeRepository.findById(request.getGradeId())
                .orElseThrow(() -> new GradeNotFoundException(
                        "Qiymət tapılmadı, id: " + request.getGradeId()));

        Appeal appeal = new Appeal();
        appeal.setStudentId(request.getStudentId());
        appeal.setGradeId(request.getGradeId());
        appeal.setMessage(request.getMessage());
        appeal.setStatus(AppealStatus.OPEN);
        appeal = appealRepository.save(appeal);

        AppealMessage firstMessage = new AppealMessage();
        firstMessage.setAppeal(appeal);
        firstMessage.setSenderId(request.getStudentId());
        firstMessage.setSenderRole("STUDENT");
        firstMessage.setMessage(request.getMessage());
        appealMessageRepository.save(firstMessage);

        log.info("Apellyasiya yaradıldı - studentId: {}, gradeId: {}",
                request.getStudentId(), request.getGradeId());
        return appealMapper.toResponse(appeal);
    }


    @Transactional
    public AppealMessageResponse sendMessage(Long appealId, SendAppealMessageRequest request) {
        Appeal appeal = appealRepository.findById(appealId)
                .orElseThrow(() -> new AppealNotFoundException(
                        "Apellyasiya tapılmadı, id: " + appealId));

        if (appeal.getStatus() == AppealStatus.CLOSED) {
            throw new AppealAlreadyClosedException("Bu apellyasiya artıq bağlanıb.");
        }

        AppealMessage message = new AppealMessage();
        message.setAppeal(appeal);
        message.setSenderId(request.getSenderId());
        message.setSenderRole(request.isTeacher() ? "TEACHER" : "STUDENT");
        message.setMessage(request.getMessage());
        message = appealMessageRepository.save(message);

        log.info("Apellyasiya mesajı saxlandı - appealId: {}, senderId: {}",
                appealId, request.getSenderId());
        return appealMapper.toMessageResponse(message);
    }

    @Transactional
    public void closeAppeal(Long appealId) {
        Appeal appeal = appealRepository.findById(appealId)
                .orElseThrow(() -> new AppealNotFoundException(
                        "Apellyasiya tapılmadı, id: " + appealId));

        if (appeal.getStatus() == AppealStatus.CLOSED) {
            throw new AppealAlreadyClosedException("Bu apellyasiya artıq bağlanıb.");
        }

        appeal.setStatus(AppealStatus.CLOSED);
        appealRepository.save(appeal);
        log.info("Apellyasiya bağlandı - appealId: {}", appealId);
    }

    public AppealResponse getAppeal(Long appealId) {
        Appeal appeal = appealRepository.findById(appealId)
                .orElseThrow(() -> new AppealNotFoundException(
                        "Apellyasiya tapılmadı, id: " + appealId));
        return appealMapper.toResponse(appeal);
    }

    public List<AppealResponse> getAppealsByStudent(Long studentId) {
        return appealRepository.findByStudentId(studentId)
                .stream()
                .map(appealMapper::toResponse)
                .collect(Collectors.toList());
    }

    public List<AppealResponse> getAppealsByGrade(Long gradeId) {
        return appealRepository.findByGradeId(gradeId)
                .stream()
                .map(appealMapper::toResponse)
                .collect(Collectors.toList());
    }

    public List<AppealMessageResponse> getMessages(Long appealId) {
        if (!appealRepository.existsById(appealId)) {
            throw new AppealNotFoundException("Apellyasiya tapılmadı, id: " + appealId);
        }
        return appealMessageRepository.findByAppealIdOrderByCreatedAtAsc(appealId)
                .stream()
                .map(appealMapper::toMessageResponse)
                .collect(Collectors.toList());
    }

    public List<AppealResponse> getOpenAppeals() {
        return appealRepository.findByStatus(AppealStatus.OPEN)
                .stream()
                .map(appealMapper::toResponse)
                .collect(Collectors.toList());
    }
}