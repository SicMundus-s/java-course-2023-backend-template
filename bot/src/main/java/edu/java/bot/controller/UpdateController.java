package edu.java.bot.controller;

import edu.java.bot.service.impl.NotificationService;
import edu.java.core.dto.RequestLinkUpdate;
import edu.java.core.dto.ResponseLinkUpdate;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v0/bot")
@RequiredArgsConstructor
public class UpdateController {

    private final NotificationService notificationService;

    @PostMapping("/updates")
    public ResponseEntity<ResponseLinkUpdate> sendUpdate(@RequestBody RequestLinkUpdate requestLinkUpdate) {
        notificationService.processNotification(requestLinkUpdate);
        return ResponseEntity.ok(new ResponseLinkUpdate("OK"));
    }
}
