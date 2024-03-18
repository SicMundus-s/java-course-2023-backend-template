package edu.java.scrapper.dao.jpa;

import edu.java.core.entity.enums.ResourceType;
import edu.java.scrapper.IntegrationEnvironment;
import edu.java.scrapper.entity.Chat;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import edu.java.scrapper.entity.Link;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JpaChatDaoIntegrationTest extends IntegrationEnvironment {

    @Autowired
    private JpaChatDao jpaChatDao;

    @Test
    @Rollback
    void whenFindByChatId_thenReturnChat() {
        Chat chat = new Chat();
        chat.setChatId(123L);
        jpaChatDao.save(chat);

        Optional<Chat> foundChat = jpaChatDao.findByChatId(123L);

        assertTrue(foundChat.isPresent());
        assertEquals(123L, foundChat.get().getChatId());
    }

    @Test
    @Rollback
    void whenFindAllByLinkId_thenReturnChatsList() {
        Link link = new Link();
        link.setUrl("https://example.com");
        link.setUpdatedAt(OffsetDateTime.now());
        link.setResourceType(ResourceType.GITHUB);
        link.setLastCheck(OffsetDateTime.now());
        link.setGithubUpdatedAt(OffsetDateTime.now());
        link.setStackoverflowLastEditDateQuestion(OffsetDateTime.now());


        Chat chat = new Chat();
        chat.setChatId(456L);
        chat.setLinks(Set.of(link));
        jpaChatDao.save(chat);
        List<Chat> foundChats = jpaChatDao.findAllByLinkId(link.getId());

        assertFalse(foundChats.isEmpty());
        assertTrue(foundChats.stream().anyMatch(c -> c.getChatId().equals(456L)));
    }
}
