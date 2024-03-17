package edu.java.scrapper.service;

import edu.java.core.dto.AddLinkRequest;
import edu.java.core.dto.LinkResponse;
import edu.java.scrapper.entity.Link;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;

public interface LinkService {

    LinkResponse add(Long chatId, AddLinkRequest addLinkRequest);

    LinkResponse remove(Long chatId, URI url);

    List<LinkResponse> listAll(Long chatId);

    List<Link> findLinksToCheck(OffsetDateTime lastCheckedBefore);

    void updateLink(Link link);
}
