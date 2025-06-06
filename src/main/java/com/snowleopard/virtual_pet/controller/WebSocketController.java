package com.snowleopard.virtual_pet.controller;

import com.snowleopard.virtual_pet.dto.response.PetResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
@RequiredArgsConstructor
public class WebSocketController {

    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/pet-update")
    @SendTo("/topic/pet-updates")
    public PetResponse handlePetUpdate(PetResponse petUpdate) {
        log.info("Broadcasting pet update for pet: {}", petUpdate.getId());
        return petUpdate;
    }

    public void sendPetUpdate(String userId, PetResponse petUpdate) {
        log.info("Sending pet update to user: {}", userId);
        messagingTemplate.convertAndSend("/topic/pet-updates/" + userId, petUpdate);
    }

    public void sendNotification(String userId, String message) {
        log.info("Sending notification to user: {}", userId);
        messagingTemplate.convertAndSend("/topic/notifications/" + userId, message);
    }
}
