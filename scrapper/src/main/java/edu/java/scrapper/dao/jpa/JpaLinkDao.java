package edu.java.scrapper.dao.jpa;

import edu.java.scrapper.entity.Link;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface JpaLinkDao extends JpaRepository<Link, Long> {
    Optional<Link> findByUrlAndChatsChatId(String url, Long chatId);

    @Query("SELECT l FROM Link l JOIN l.chats c WHERE c.chatId = :chatId")
    List<Link> findAllByChatId(@Param("chatId") Long chatId);

    @Query("SELECT l FROM Link l WHERE l.lastCheck < :lastCheckedBefore")
    List<Link> findAllWithLastCheckBefore(@Param("lastCheckedBefore") OffsetDateTime lastCheckedBefore);
}
