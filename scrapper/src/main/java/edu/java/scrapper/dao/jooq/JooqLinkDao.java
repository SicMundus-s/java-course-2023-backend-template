package edu.java.scrapper.dao.jooq;

import edu.java.core.exception.BadRequestException;
import edu.java.core.exception.NotFoundException;
import edu.java.scrapper.domain.jooq.enums.ResourceType;
import edu.java.scrapper.domain.jooq.tables.records.LinksRecord;
import edu.java.scrapper.entity.Link;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Nullable;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import static edu.java.scrapper.domain.jooq.Tables.CHAT_LINKS;
import static edu.java.scrapper.domain.jooq.Tables.LINKS;

@Repository
@RequiredArgsConstructor
public class JooqLinkDao {

    private final DSLContext dsl;

    @Transactional
    public Link save(long chatId, Link link) {
        boolean exists = dsl.selectFrom(LINKS.join(CHAT_LINKS)
                .on(LINKS.ID.eq(CHAT_LINKS.LINK_ID)))
            .where(LINKS.URL.eq(link.getUrl()).and(CHAT_LINKS.CHAT_ID.eq(chatId)))
            .fetch().isNotEmpty();

        if (exists) {
            throw new BadRequestException("URL already exists for this chat.");
        }

        @Nullable LinksRecord linksRecord = dsl.insertInto(LINKS, LINKS.URL, LINKS.RESOURCE_TYPE)
            .values(link.getUrl(), ResourceType.valueOf(link.getResourceType().name()))
            .returning(LINKS.ID)
            .fetchOne();

        assert linksRecord != null;
        Long linkId = linksRecord.getValue(LINKS.ID);

        dsl.insertInto(CHAT_LINKS, CHAT_LINKS.CHAT_ID, CHAT_LINKS.LINK_ID)
            .values(chatId, linkId)
            .execute();

        return findLinkById(linkId);
    }

    @Transactional
    public Link delete(long chatId, URI url) {
        Long linkId = dsl.select(LINKS.ID)
            .from(LINKS)
            .where(LINKS.URL.eq(url.toString()))
            .fetchOneInto(Long.class);

        if (linkId == null) {
            throw new NotFoundException("Link not found");
        }

        dsl.deleteFrom(CHAT_LINKS)
            .where(CHAT_LINKS.CHAT_ID.eq(chatId).and(CHAT_LINKS.LINK_ID.eq(linkId)))
            .execute();

        return findLinkById(linkId);
    }

    public List<Link> findAll(long chatId) {
        return dsl.select(LINKS.fields())
            .from(LINKS)
            .join(CHAT_LINKS).on(LINKS.ID.eq(CHAT_LINKS.LINK_ID))
            .where(CHAT_LINKS.CHAT_ID.eq(chatId))
            .fetchInto(Link.class);
    }

    public List<Link> findAll(OffsetDateTime lastCheckedBefore) {
        return dsl.selectFrom(LINKS)
            .where(LINKS.LAST_CHECK.lt(lastCheckedBefore))
            .fetchInto(Link.class);
    }

    public void update(Link link) {
        dsl.update(LINKS)
            .set(LINKS.URL, link.getUrl())
            .set(LINKS.RESOURCE_TYPE, ResourceType.valueOf(link.getResourceType().name()))
            .set(LINKS.LAST_CHECK, link.getLastCheck())
            .set(LINKS.GITHUB_UPDATED_AT, link.getGithubUpdatedAt())
            .set(LINKS.STACKOVERFLOW_LAST_EDIT_DATE_QUESTION, link.getStackoverflowLastEditDateQuestion())
            .where(LINKS.ID.eq(link.getId()))
            .execute();
    }

    private Link findLinkById(Long id) {
        return dsl.selectFrom(LINKS)
            .where(LINKS.ID.eq(id))
            .fetchOneInto(Link.class);
    }
}
