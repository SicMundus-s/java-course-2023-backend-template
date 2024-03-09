package edu.java.scrapper.service;

import edu.java.core.dto.LinkResponse;
import edu.java.core.dto.ListLinksResponse;
import edu.java.scrapper.aop.CheckRegistration;
import java.net.URI;
import java.util.List;
import org.intellij.lang.annotations.MagicConstant;
import org.springframework.stereotype.Service;

@Service
public class LinkService {
    @CheckRegistration
    @MagicConstant
    public ListLinksResponse getLinks(Long chatId) {
        return new ListLinksResponse(List.of(), chatId.intValue());
    }

    @CheckRegistration
    public LinkResponse addLink(Long chatId, URI uri) {
        return new LinkResponse(chatId, uri);
    }

    @CheckRegistration
    public LinkResponse removeLink(Long chatId, URI uri) {
        // ToDo "404" "Ссылка не найдена",
        return new LinkResponse(chatId, uri);
    }
}
