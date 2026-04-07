// grade-service/controller/AppealChatController.java
package org.example.gradeservice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.gradeservice.Service.AppealChatService;
import org.example.gradeservice.dto.AppealMessageResponse;
import org.example.gradeservice.dto.ChatMessageDto;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
@RequiredArgsConstructor
public class AppealChatController {

    private final AppealChatService appealChatService;


    @MessageMapping("/appeal.send")
    public void sendMessage(@Payload ChatMessageDto dto, Authentication auth) {
        Long senderId = (Long) auth.getPrincipal();
        String role   = auth.getAuthorities().iterator().next()
                .getAuthority().replace("ROLE_", "");
        log.info("WebSocket mesaj alındı - appealId:{}, senderId:{}, role:{}",
                dto.getAppealId(), senderId, role);
        appealChatService.processAndBroadcast(dto, senderId, role);
    }
}