package edu.java.scrapper.dao.jdbc;

import edu.java.core.exception.BadRequestException;
import edu.java.core.exception.NotFoundException;
import edu.java.scrapper.entity.Link;
import edu.java.scrapper.mapper.rowmapper.LinkRowMapper;
import java.net.URI;
import java.sql.PreparedStatement;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class JdbcLinkDao {

    private final JdbcTemplate jdbcTemplate;
    private final LinkRowMapper linkRowMapper;

    private static final String SELECT_LINK =
        "SELECT * FROM links where id = ?";
    private static final String SELECT_LINK_BY_СHATID_AND_URL =
        "SELECT * FROM links l "
            + "JOIN chat_links cl ON l.id = cl.link_id "
            + "JOIN chats c ON cl.chat_id = c.id "
            + "WHERE l.url = ? AND c.chat_id = ?";

    private static final String INSERT_CHAT_LINKS = "INSERT INTO chat_links (chat_id, link_id) VALUES (?, ?)";
    private static final String INSERT_LINK = "INSERT INTO links (url, resource_type) VALUES (?, ?::resource_type)";
    private static final String SELECT_ALL_LINKS =
        "SELECT * FROM links l "
            + "JOIN chat_links cl ON l.id = cl.link_id "
            + "JOIN chats c ON cl.chat_id = c.id "
            + "WHERE c.chat_id = ?;";

    @Transactional
    public Link save(long actualChatId, Link link) {
        List<Link> saveLink =
            jdbcTemplate.query(SELECT_LINK_BY_СHATID_AND_URL, linkRowMapper, link.getUrl(), actualChatId);

        if (!saveLink.isEmpty()) {
            throw new BadRequestException("URL already exists for this chat.");
        }

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(INSERT_LINK, new String[] {"id"});
            ps.setString(1, link.getUrl());
            ps.setString(2, link.getResourceType().name());
            return ps;
        }, keyHolder);
        long linkPrimaryKeyId = keyHolder.getKey().longValue();

        Long chatPrimaryKeyId = jdbcTemplate.queryForObject(
            "SELECT id FROM chats WHERE chat_id = ?", Long.class, actualChatId
        );

        jdbcTemplate.update(INSERT_CHAT_LINKS, chatPrimaryKeyId, linkPrimaryKeyId);

        return jdbcTemplate.queryForObject(SELECT_LINK, linkRowMapper, linkPrimaryKeyId);
    }

    @Transactional
    public Link delete(long chatId, URI url) {
        List<Link> saveLink = jdbcTemplate.query(SELECT_LINK_BY_СHATID_AND_URL, linkRowMapper, url.toString(), chatId);
        if (saveLink.isEmpty()) {
            throw new NotFoundException("Link not found");
        }
        Link singlLink = saveLink.getFirst();
        String deleteRowChatLink = "DELETE FROM links where id = ?";

        jdbcTemplate.update(deleteRowChatLink, singlLink.getId());
        return singlLink;
    }

    public List<Link> findAll(long chatId) {
        List<Link> saveLinks = jdbcTemplate.query(SELECT_ALL_LINKS, linkRowMapper, chatId);
        if (saveLinks.isEmpty()) {
            throw new NotFoundException("Links not found");
        }
        return saveLinks;
    }

    public List<Link> findAll(OffsetDateTime lastCheckedBefore) {
        String selectLinksBeforeLastCheck =
            "SELECT * FROM links where last_check < ?";
        return jdbcTemplate.query(selectLinksBeforeLastCheck, linkRowMapper, lastCheckedBefore);

    }

    public void update(Link link) {
        String sqlUpdate =
            "UPDATE links SET url = ?, updated_at = ?, last_check = ?, resource_type = ?::resource_type, "
                + "github_updated_at = ?, stackoverflow_last_edit_date_question = ? WHERE id = ?";
        jdbcTemplate.update(
            sqlUpdate,
            link.getUrl(),
            link.getUpdatedAt(),
            link.getLastCheck(),
            link.getResourceType().name(),
            link.getGithubUpdatedAt(),
            link.getStackoverflowLastEditDateQuestion(),
            link.getId()
        );
    }
}
