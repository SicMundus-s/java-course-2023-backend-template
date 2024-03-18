package edu.java.scrapper.service.impl.jdbc;

import edu.java.core.dto.AddLinkRequest;
import edu.java.core.dto.LinkResponse;
import edu.java.scrapper.aop.CheckRegistration;
import edu.java.scrapper.dao.jdbc.JdbcLinkDao;
import edu.java.scrapper.entity.Link;
import edu.java.scrapper.mapper.LinkMapper;
import edu.java.scrapper.service.LinkService;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JdbcLinkServiceImpl implements LinkService {

    private final JdbcLinkDao jdbcLinkDao;
    private final LinkMapper linkMapper;

    @Override
    @CheckRegistration
    public LinkResponse add(Long chatId, AddLinkRequest addLinkRequest) {
        Link link = linkMapper.toEntity(addLinkRequest);
        return linkMapper.toDto(jdbcLinkDao.save(chatId, link));
    }

    @Override
    @CheckRegistration
    public LinkResponse remove(Long chatId, URI url) {
        return linkMapper.toDto(jdbcLinkDao.delete(chatId, url));
    }

    @Override
    @CheckRegistration
    public List<LinkResponse> listAll(Long chatId) {
        return jdbcLinkDao.findAll(chatId).stream().map(linkMapper::toDto).toList();
    }

    @Override
    public List<Link> findLinksToCheck(OffsetDateTime lastCheckedBefore) {
        return jdbcLinkDao.findAll(lastCheckedBefore);
    }

    @Override
    public void updateLink(Link link) {
        jdbcLinkDao.update(link);
    }
}
