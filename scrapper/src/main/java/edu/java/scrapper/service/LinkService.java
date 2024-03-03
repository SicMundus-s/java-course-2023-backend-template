package edu.java.scrapper.service;

import edu.java.core.dto.LinkResponse;
import edu.java.core.dto.ListLinksResponse;
import edu.java.scrapper.aop.CheckRegistration;
import org.springframework.stereotype.Service;
import java.net.URI;
import java.util.List;
@Service
public class LinkService {
    @CheckRegistration
    public ListLinksResponse getLinks(Long chatId) {
        return new ListLinksResponse(List.of(), 111);
    }
    @CheckRegistration
    public LinkResponse addLink(Long chatId, URI uri) {
        return new LinkResponse(111L, uri);
    }
    @CheckRegistration
    public LinkResponse removeLink(Long chatId, URI uri) {
        // ToDo "404" "Ссылка не найдена",
        return new LinkResponse(112L, uri);
    }
}
