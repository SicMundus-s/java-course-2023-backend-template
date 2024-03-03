package edu.java.scrapper.controller;

import edu.java.core.dto.AddLinkRequest;
import edu.java.core.dto.LinkResponse;
import edu.java.core.dto.ListLinksResponse;
import edu.java.core.dto.RemoveLinkRequest;
import edu.java.core.exception.BadRequestException;
import edu.java.scrapper.service.LinkService;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v0/chats")
@RequiredArgsConstructor
public class LinkController {

    private final LinkService linkService;

    @GetMapping("/links")
    public ResponseEntity<ListLinksResponse> getLinks(@RequestHeader("Tg-Chat-Id") String chatId) {
        Long id = Long.valueOf(chatId);
        idIsIncorrect(id);
        return ResponseEntity.ok(linkService.getLinks(id));

    }

    @PostMapping("/links")
    public ResponseEntity<LinkResponse> addLink(@RequestHeader("Tg-Chat-Id") String chatId,
                                                @RequestBody AddLinkRequest request) {
        Long id = Long.valueOf(chatId);
        idIsIncorrect(id);
        LinkResponse link;
        try {
            link = linkService.addLink(id, URI.create(request.link()));
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Incorrect link");
        }
        return ResponseEntity.ok(link);
    }

    @DeleteMapping("/links")
    public ResponseEntity<LinkResponse> deleteLink(@RequestHeader("Tg-Chat-Id") String chatId,
                                                   @RequestBody RemoveLinkRequest request) {
        Long id = Long.valueOf(chatId);
        idIsIncorrect(id);
        LinkResponse link;
        try {
            link = linkService.removeLink(id, URI.create(request.link()));
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Incorrect link");
        }
        return ResponseEntity.ok(link);
    }

    private void idIsIncorrect(Long id) {
        if (id == null || id <= 0) {
            throw new BadRequestException("id must be greater than zero");
        }
    }
}
