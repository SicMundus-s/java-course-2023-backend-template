package edu.java.scrapper.dao.jpa;

import edu.java.core.dto.AddLinkRequest;
import edu.java.core.dto.LinkResponse;
import edu.java.core.entity.enums.ResourceType;
import edu.java.scrapper.IntegrationEnvironment;
import edu.java.scrapper.entity.Chat;
import edu.java.scrapper.entity.Link;
import edu.java.scrapper.mapper.LinkMapper;
import edu.java.scrapper.service.impl.jpa.JpaLinkServiceImpl;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import static edu.java.scrapper.service.impl.Utils.createAddLinkRequest;
import static edu.java.scrapper.service.impl.Utils.createTestChat;
import static edu.java.scrapper.service.impl.Utils.createTestLink;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class JpaLinkDaoIntegrationTest extends IntegrationEnvironment {

    @Autowired
    private JpaLinkDao jpaLinkDao;

    @Autowired
    private JpaChatDao jpaChatDao;

    @Autowired
    private LinkMapper linkMapper;

    private JpaLinkServiceImpl linkService;

    @BeforeEach
    void setUp() {
        linkService = new JpaLinkServiceImpl(jpaLinkDao, jpaChatDao, linkMapper);
    }

    @Test
    @Transactional
    @Rollback
    void whenAddLink_thenItShouldBeAdded() {
        Chat chat = createTestChat();
        jpaChatDao.save(chat);
        chat.setLinks(new HashSet<>());
        AddLinkRequest addLinkRequest = createAddLinkRequest("https://example.com", ResourceType.GITHUB);

        LinkResponse addedLink = linkService.add(chat.getChatId(), addLinkRequest);

        assertNotNull(addedLink);
        assertEquals(addLinkRequest.getLink(), addedLink.url().toString());
    }

    @Test
    @Transactional
    @Rollback
    void whenRemoveLink_thenItShouldBeRemoved() {
        Chat chat = createTestChat();
        Link link = createTestLink(chat);
        jpaChatDao.save(chat);
        jpaLinkDao.save(link);

        assertDoesNotThrow(() -> linkService.remove(chat.getChatId(), URI.create(link.getUrl())));
    }

    @Test
    @Transactional
    @Rollback
    void whenListAll_thenItShouldReturnAllLinks() {
        Chat chat = createTestChat();
        Link link = createTestLink(chat);
        jpaChatDao.save(chat);
        jpaLinkDao.save(link);

        List<LinkResponse> links = linkService.listAll(chat.getChatId());

        assertFalse(links.isEmpty());
        assertEquals(1, links.size());
        assertEquals(link.getUrl(), links.getFirst().url().toString());
    }

    @Test
    @Transactional
    @Rollback
    void whenUpdateLink_thenItShouldBeUpdated() {
        Chat chat = createTestChat();
        Link link = createTestLink(chat);
        jpaChatDao.save(chat);
        jpaLinkDao.save(link);

        link.setResourceType(ResourceType.STACKOVERFLOW);
        linkService.updateLink(link);

        Link updatedLink = jpaLinkDao.findById(link.getId()).orElseThrow();
        assertEquals(ResourceType.STACKOVERFLOW, updatedLink.getResourceType());
    }

}
