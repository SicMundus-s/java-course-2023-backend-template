package edu.java.scrapper.service.impl.jooq;

import edu.java.core.dto.AddLinkRequest;
import edu.java.core.dto.LinkResponse;
import edu.java.scrapper.aop.CheckRegistration;
import edu.java.scrapper.dao.jooq.JooqLinkDao;
import edu.java.scrapper.entity.Link;
import edu.java.scrapper.mapper.LinkMapper;
import edu.java.scrapper.service.LinkService;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class JooqLinkServiceImpl implements LinkService {

    private final JooqLinkDao jooqLinkDao;
    private final LinkMapper linkMapper;

    @Override
    @Transactional
    @CheckRegistration
    public LinkResponse add(Long chatId, AddLinkRequest addLinkRequest) {
        Link link = linkMapper.toEntity(addLinkRequest);
        Link savedLink = jooqLinkDao.save(chatId, link);
        return linkMapper.toDto(savedLink);
    }

    @Override
    @Transactional
    @CheckRegistration
    public LinkResponse remove(Long chatId, URI url) {
        Link deletedLink = jooqLinkDao.delete(chatId, url);
        return linkMapper.toDto(deletedLink);
    }

    @Override
    public List<LinkResponse> listAll(Long chatId) {
        List<Link> links = jooqLinkDao.findAll(chatId);
        return links.stream().map(linkMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public List<Link> findLinksToCheck(OffsetDateTime lastCheckedBefore) {
        return jooqLinkDao.findAll(lastCheckedBefore);
    }

    @Override
    @Transactional
    public void updateLink(Link link) {
        jooqLinkDao.update(link);
    }
}
