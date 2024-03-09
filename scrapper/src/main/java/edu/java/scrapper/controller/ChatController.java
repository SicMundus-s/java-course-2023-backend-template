package edu.java.scrapper.controller;

import edu.java.core.dto.ResponseChat;
import edu.java.core.exception.BadRequestException;
import edu.java.scrapper.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v0/chats")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @PostMapping("/tg-chat/{id}")
    public ResponseEntity<ResponseChat> registerChat(@PathVariable Long id) {
        idIsIncorrect(id);
        chatService.registreation(id);
        return ResponseEntity.ok(new ResponseChat("Chat registered"));
    }

    @DeleteMapping("/tg-chat/{id}")
    public ResponseEntity<ResponseChat> deleteChat(@PathVariable Long id) {
        idIsIncorrect(id);
        chatService.delete(id);
        return ResponseEntity.ok(new ResponseChat("Chat successfully deleted"));
    }

    private void idIsIncorrect(Long id) {
        if (id == null || id <= 0) {
            throw new BadRequestException("id must be greater than zero");
        }
    }
}
