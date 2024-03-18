package edu.java.scrapper.service.impl.jpa;

import edu.java.core.dto.AddLinkRequest;
import edu.java.core.dto.LinkResponse;
import edu.java.core.entity.enums.ResourceType;
import edu.java.scrapper.IntegrationEnvironment;
import edu.java.scrapper.dao.jpa.JpaChatDao;
import edu.java.scrapper.dao.jpa.JpaLinkDao;
import edu.java.scrapper.entity.Chat;
import edu.java.scrapper.entity.Link;
import edu.java.scrapper.mapper.LinkMapper;
import java.net.URI;
import java.util.HashSet;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import static edu.java.scrapper.service.impl.Utils.createAddLinkRequest;
import static edu.java.scrapper.service.impl.Utils.createTestChat;
import static edu.java.scrapper.service.impl.Utils.createTestLink;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JpaLinkServiceImplTest extends IntegrationEnvironment {

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
        chat.setLinks(new HashSet<>());
        jpaChatDao.save(chat);
        AddLinkRequest request = createAddLinkRequest("https://example.com", ResourceType.GITHUB);

        LinkResponse response = linkService.add(chat.getChatId(), request);

        assertNotNull(response);
        assertEquals(request.getLink(), response.url().toString());

        List<Link> links = jpaLinkDao.findAllByChatId(chat.getChatId());
        assertFalse(links.isEmpty());
    }

    @Test
    @Transactional
    @Rollback
    void whenRemoveLink_thenItShouldBeRemoved() {
        Chat chat = createTestChat();
        Link link = createTestLink(chat);
        jpaChatDao.save(chat);
        jpaLinkDao.save(link);

        linkService.remove(chat.getChatId(), URI.create(link.getUrl()));

        assertTrue(jpaLinkDao.findByUrlAndChatsChatId(link.getUrl(), chat.getId()).isEmpty());
    }

    @Test
    @Transactional
    @Rollback
    void whenListAll_thenItShouldReturnAllLinksForChat() {
        Chat chat = createTestChat();
        Link link = createTestLink(chat);
        jpaChatDao.save(chat);
        jpaLinkDao.save(link);

        List<LinkResponse> links = linkService.listAll(chat.getChatId());

        assertFalse(links.isEmpty());
        assertEquals(1, links.size());
        assertEquals(link.getUrl(), links.getFirst().url().toString());
    }

}
