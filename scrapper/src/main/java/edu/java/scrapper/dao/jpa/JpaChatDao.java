package edu.java.scrapper.dao.jpa;

import edu.java.scrapper.entity.Chat;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaChatDao extends JpaRepository<Chat, Long> {
    Optional<Chat> findByChatId(Long chatId);

    @Query("SELECT c FROM Chat c JOIN c.links l WHERE l.id = :linkId")
    List<Chat> findAllByLinkId(@Param("linkId") Long linkId);
}
