package edu.java.scrapper.service.impl.jpa;

import edu.java.core.dto.AddLinkRequest;
import edu.java.core.dto.LinkResponse;
import edu.java.core.exception.BadRequestException;
import edu.java.core.exception.NotFoundException;
import edu.java.scrapper.aop.CheckRegistration;
import edu.java.scrapper.dao.jpa.JpaChatDao;
import edu.java.scrapper.dao.jpa.JpaLinkDao;
import edu.java.scrapper.entity.Chat;
import edu.java.scrapper.entity.Link;
import edu.java.scrapper.mapper.LinkMapper;
import edu.java.scrapper.service.LinkService;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class JpaLinkServiceImpl implements LinkService {

    private final JpaLinkDao jpaLinkDao;
    private final JpaChatDao jpaChatDao;
    private final LinkMapper linkMapper;

    @Override
    @CheckRegistration
    @Transactional
    public LinkResponse add(Long chatId, AddLinkRequest addLinkRequest) {
        Link link = linkMapper.toEntity(addLinkRequest);
        Optional<Link> existingLink = jpaLinkDao.findByUrlAndChatsChatId(link.getUrl(), chatId);
        if (existingLink.isPresent()) {
            throw new BadRequestException("URL already exists for this chat.");
        }

        Chat chat = jpaChatDao.findByChatId(chatId)
            .orElseThrow(() -> new NotFoundException("Chat not found"));
        chat.getLinks().add(link);
        jpaChatDao.save(chat);

        return linkMapper.toDto(link);
    }

    @Override
    @CheckRegistration
    @Transactional
    public LinkResponse remove(Long chatId, URI url) {
        Optional<Link> existingLink = jpaLinkDao.findByUrlAndChatsChatId(url.toString(), chatId);
        if (existingLink.isEmpty()) {
            throw new NotFoundException("Link not found");
        }
        Link link = existingLink.get();
        jpaLinkDao.delete(link);
        return linkMapper.toDto(link);
    }

    @Override
    @CheckRegistration
    public List<LinkResponse> listAll(Long chatId) {
        return jpaLinkDao.findAllByChatId(chatId).stream().map(linkMapper::toDto).toList();
    }

    @Override
    public List<Link> findLinksToCheck(OffsetDateTime lastCheckedBefore) {
        return jpaLinkDao.findAllWithLastCheckBefore(lastCheckedBefore);
    }

    @Override
    public void updateLink(Link link) {
        jpaLinkDao.save(link);
    }
}
