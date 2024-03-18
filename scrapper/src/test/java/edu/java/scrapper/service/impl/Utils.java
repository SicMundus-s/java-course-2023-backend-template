package edu.java.scrapper.service.impl;

import edu.java.core.dto.AddLinkRequest;
import edu.java.core.entity.enums.ResourceType;
import edu.java.scrapper.entity.Chat;
import edu.java.scrapper.entity.Link;
import java.time.OffsetDateTime;
import java.util.Set;

public class Utils {

    public static Chat createTestChat() {
        Chat chat = new Chat();
        chat.setChatId(12345L);
        return chat;
    }

    public static Link createTestLink(Chat chat) {
        Link link = new Link();
        link.setUrl("https://example.com");
        link.setUpdatedAt(OffsetDateTime.now());
        link.setResourceType(ResourceType.GITHUB);
        link.setLastCheck(OffsetDateTime.now());
        link.setGithubUpdatedAt(OffsetDateTime.now());
        link.setStackoverflowLastEditDateQuestion(OffsetDateTime.now());
        chat.setLinks(Set.of(link));
        return link;
    }

    public static AddLinkRequest createAddLinkRequest(String url, ResourceType resourceType) {
        AddLinkRequest addLinkRequest = new AddLinkRequest();
        addLinkRequest.setLink(url);
        addLinkRequest.setResourceType(resourceType);
        return addLinkRequest;
    }
}
