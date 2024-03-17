package edu.java.scrapper.dao.jooq;

import edu.java.core.exception.BadRequestException;
import edu.java.core.exception.NotFoundException;
import edu.java.scrapper.entity.Chat;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;
import static edu.java.scrapper.domain.jooq.tables.ChatLinks.CHAT_LINKS;
import static edu.java.scrapper.domain.jooq.tables.Chats.CHATS;

@Repository
@RequiredArgsConstructor
public class JooqChatDao {

    private final DSLContext dsl;

    public void register(long chatId) {
        if (isChatRegistered(chatId)) {
            throw new BadRequestException("Chat is already registered");
        }

        dsl.insertInto(CHATS)
            .set(CHATS.CHAT_ID, chatId)
            .execute();
    }

    public void removeChat(long chatId) {
        var deleted = dsl.deleteFrom(CHATS)
            .where(CHATS.CHAT_ID.eq(chatId))
            .execute();

        if (deleted == 0) {
            throw new NotFoundException("Chat with ID: " + chatId + " does not exist");
        }
    }

    public List<Chat> findAllChatsByLinkId(Long linkId) {
        return dsl.select(CHATS.ID, CHATS.CHAT_ID)
            .from(CHATS)
            .join(CHAT_LINKS).on(CHATS.ID.eq(CHAT_LINKS.CHAT_ID))
            .where(CHAT_LINKS.LINK_ID.eq(linkId))
            .fetchInto(Chat.class);
    }

    public Chat findChatByChatId(Long chatId) {
        return dsl.selectFrom(CHATS)
            .where(CHATS.CHAT_ID.eq(chatId))
            .fetchOneInto(Chat.class);
    }

    public boolean isChatRegistered(Long chatId) {
        return dsl.fetchExists(
            dsl.selectFrom(CHATS)
                .where(CHATS.CHAT_ID.eq(chatId))
        );
    }
}
