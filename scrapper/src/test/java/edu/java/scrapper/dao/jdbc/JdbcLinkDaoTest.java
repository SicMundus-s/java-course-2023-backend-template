package edu.java.scrapper.dao.jdbc;

import edu.java.core.entity.enums.ResourceType;
import edu.java.scrapper.IntegrationEnvironment;
import edu.java.scrapper.entity.Link;
import edu.java.scrapper.mapper.rowmapper.LinkRowMapper;
import java.net.URI;
import java.sql.PreparedStatement;
import java.time.OffsetDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class JdbcLinkDaoTest extends IntegrationEnvironment {

    @Autowired
    private JdbcLinkDao linkDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private Link createTestLink() {
        Link link = new Link();
        link.setId(0L);
        link.setUrl("https://example.com");
        link.setUpdatedAt(OffsetDateTime.now());
        link.setLastCheck(OffsetDateTime.now());
        link.setResourceType(ResourceType.GITHUB);
        return link;
    }

    @Test
    @Transactional
    @Rollback
    void saveTest() {
        long actualChatId = 12345L;
        Link testLink = createTestLink();

        jdbcTemplate.update("INSERT INTO chats (chat_id) VALUES (?)", actualChatId);

        Link savedLink = linkDao.save(actualChatId, testLink);
        assertNotNull(savedLink);
        assertNotNull(savedLink.getId());
        assertEquals(testLink.getUrl(), savedLink.getUrl());

        List<Link> links = jdbcTemplate.query(
            "SELECT * FROM links WHERE id = ?",
            new LinkRowMapper(),
            savedLink.getId()
        );
        assertFalse(links.isEmpty());
        assertEquals(testLink.getUrl(), links.getFirst().getUrl());
    }

    @Test
    @Transactional
    @Rollback
    void deleteTest() {
        long chatId = 67890L;
        jdbcTemplate.update("INSERT INTO chats (chat_id) VALUES (?)", chatId);
        Link link = createTestLink();
        Link savedLink = linkDao.save(chatId, link);

        Link deletedLink = linkDao.delete(chatId, URI.create(savedLink.getUrl()));
        assertNotNull(deletedLink);

        List<Link> linksAfterDeletion = jdbcTemplate.query(
            "SELECT id FROM links WHERE url = ?",
            new LinkRowMapper(),
            link.getUrl()
        );
        assertTrue(linksAfterDeletion.isEmpty());
    }

    @Test
    @Transactional
    @Rollback
    void findAllTest() {
        long chatId = 12345L;
        jdbcTemplate.update("INSERT INTO chats (chat_id) VALUES (?)", chatId);

        Link link1 = createTestLink();
        Link link2 = createTestLink();
        link2.setId(1L);
        link2.setUrl("https://example2.com");
        linkDao.save(chatId, link1);
        linkDao.save(chatId, link2);

        List<Link> foundLinks = linkDao.findAll(chatId);
        assertFalse(foundLinks.isEmpty());
        assertTrue(foundLinks.size() >= 2);
    }

    @Test
    @Transactional
    @Rollback
    void updateTest() {
        long chatId = 12345L;
        jdbcTemplate.update("INSERT INTO chats (chat_id) VALUES (?)", chatId);
        Link link = createTestLink();
        Link savedLink = linkDao.save(chatId, link);

        savedLink.setUrl("https://example-updated.com");
        linkDao.update(savedLink);

        Link updatedLink = jdbcTemplate.queryForObject(
            "SELECT * FROM links WHERE id = ?",
            new LinkRowMapper(),
            savedLink.getId()
        );
        assertNotNull(updatedLink);
        assertEquals("https://example-updated.com", updatedLink.getUrl());
    }

    @Test
    @Transactional
    @Rollback
    void findAllBeforeLastCheckTest() {
        jdbcTemplate.update(
            "INSERT INTO links (url, resource_type, last_check) VALUES (?, ?::resource_type, ?)",
            "http://old-link.com",
            "GITHUB",
            OffsetDateTime.now().minusDays(2)
        );
        jdbcTemplate.update(
            "INSERT INTO links (url, resource_type, last_check) VALUES (?, ?::resource_type, ?)",
            "http://new-link.com",
            "GITHUB",
            OffsetDateTime.now()
        );

        List<Link> links = linkDao.findAll(OffsetDateTime.now().minusDays(1));
        assertEquals(1, links.size());
        assertEquals("http://old-link.com", links.getFirst().getUrl());
    }

    @Test
    @Transactional
    @Rollback
    void updateLinkTest() {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO links (url, resource_type, last_check) VALUES (?, ?::resource_type, ?)",
                new String[] {"id"}
            );
            ps.setString(1, "http://example.com");
            ps.setString(2, "GITHUB");
            ps.setObject(3, OffsetDateTime.now());
            return ps;
        }, keyHolder);
        long linkId = keyHolder.getKey().longValue();

        Link linkToUpdate = new Link();
        linkToUpdate.setId(linkId);
        linkToUpdate.setUrl("http://updated-example.com");
        linkToUpdate.setUpdatedAt(OffsetDateTime.now());
        linkToUpdate.setLastCheck(OffsetDateTime.now().minusDays(1));
        linkToUpdate.setResourceType(ResourceType.GITHUB);
        linkToUpdate.setGithubUpdatedAt(OffsetDateTime.now());
        linkToUpdate.setStackoverflowLastEditDateQuestion(OffsetDateTime.now());
        linkDao.update(linkToUpdate);

        Link updatedLink = jdbcTemplate.queryForObject("SELECT * FROM links WHERE id = ?",
            new LinkRowMapper(), linkId
        );
        assertNotNull(updatedLink);
        assertEquals("http://updated-example.com", updatedLink.getUrl());
        assertEquals(linkToUpdate.getLastCheck().toLocalDate(), updatedLink.getLastCheck().toLocalDate());
    }
}
