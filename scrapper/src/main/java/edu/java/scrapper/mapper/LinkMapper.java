package edu.java.scrapper.mapper;

import edu.java.core.dto.AddLinkRequest;
import edu.java.core.dto.LinkResponse;
import edu.java.core.dto.RequestLinkUpdate;
import edu.java.scrapper.entity.Link;
import java.net.URI;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class LinkMapper {

    public LinkResponse toDto(Link link) {
        return new LinkResponse(link.getId(), URI.create(link.getUrl()));
    }

    public Link toEntity(AddLinkRequest addLinkRequest) {
        Link link = new Link();
        link.setUrl(addLinkRequest.getLink());
        link.setResourceType(addLinkRequest.getResourceType());
        return link;
    }

    public RequestLinkUpdate toDtoUpdate(Link link, List<Long> tgChatIds, String description) {
        return new RequestLinkUpdate(
            link.getId(),
            link.getUrl(),
            "url: " + link.getUrl() + " has been updated, details: \n" + description,
            tgChatIds
        );
    }
}
