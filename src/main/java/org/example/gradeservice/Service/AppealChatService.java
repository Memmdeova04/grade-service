package org.example.gradeservice.Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.gradeservice.dto.AppealMessageResponse;
import org.example.gradeservice.dto.ChatMessageDto;
import org.example.gradeservice.entity.Appeal;
import org.example.gradeservice.entity.AppealMessage;
import org.example.gradeservice.exception.AppealAlreadyClosedException;
import org.example.gradeservice.exception.AppealNotFoundException;
import org.example.gradeservice.mapper.AppealMapper;
import org.example.gradeservice.repository.AppealMessageRepository;
import org.example.gradeservice.repository.AppealRepository;
import org.example.gradeservice.util.AppealStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AppealChatService {

    private final AppealRepository appealRepository;
    private final AppealMessageRepository appealMessageRepository;
    private final AppealMapper appealMapper;
    private final SimpMessagingTemplate messagingTemplate;

    @Transactional
    public void processAndBroadcast(ChatMessageDto dto, Long senderId, String role) {
        Appeal appeal = appealRepository.findById(dto.getAppealId())
                .orElseThrow(() -> new AppealNotFoundException(
                        "Apellyasiya tapılmadı, id: " + dto.getAppealId()));

        if (appeal.getStatus() == AppealStatus.CLOSED) {
            throw new AppealAlreadyClosedException("Bu apellyasiya bağlanıb.");
        }


        AppealMessage msg = new AppealMessage();
        msg.setAppeal(appeal);
        msg.setSenderId(senderId);
        msg.setSenderRole(role);
        msg.setMessage(dto.getMessage());
        msg = appealMessageRepository.save(msg);

        // Hər iki tərəfə broadcast et
        AppealMessageResponse response = appealMapper.toMessageResponse(msg);
        messagingTemplate.convertAndSend(
                "/topic/appeal." + dto.getAppealId(), response);

        log.info("Broadcast edildi → /topic/appeal.{}", dto.getAppealId());
    }
}