package edu.java.scrapper.service.impl.jdbc;

import edu.java.core.dto.AddLinkRequest;
import edu.java.core.dto.LinkResponse;
import edu.java.core.entity.enums.ResourceType;
import edu.java.scrapper.dao.jdbc.JdbcLinkDao;
import edu.java.scrapper.entity.Link;
import edu.java.scrapper.mapper.LinkMapper;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JdbcLinkServiceImplTest {

    @Mock
    private JdbcLinkDao jdbcLinkDao;

    @Mock
    private LinkMapper linkMapper;

    @InjectMocks
    private JdbcLinkServiceImpl linkService;

    @Test
    void addLinkCallsDaoSaveAndMapperCorrectly() {
        AddLinkRequest addLinkRequest = new AddLinkRequest();
        addLinkRequest.setLink("http://example.com");
        Link link = new Link();
        LinkResponse expectedResponse = new LinkResponse(1L, URI.create("http://example.com"));

        when(linkMapper.toEntity(any(AddLinkRequest.class))).thenReturn(link);
        when(jdbcLinkDao.save(anyLong(), any(Link.class))).thenReturn(link);
        when(linkMapper.toDto(any(Link.class))).thenReturn(expectedResponse);

        LinkResponse response = linkService.add(1L, addLinkRequest);

        verify(linkMapper, times(1)).toEntity(addLinkRequest);
        verify(jdbcLinkDao, times(1)).save(1L, link);
        verify(linkMapper, times(1)).toDto(link);

        assert response.equals(expectedResponse);
    }

    @Test
    void removeLinkCallsDaoDeleteAndMapperCorrectly() {
        URI url = URI.create("http://example.com");
        Link link = new Link();
        LinkResponse expectedResponse = new LinkResponse(1L, URI.create("http://example.com"));

        when(jdbcLinkDao.delete(anyLong(), any(URI.class))).thenReturn(link);
        when(linkMapper.toDto(any(Link.class))).thenReturn(expectedResponse);

        LinkResponse response = linkService.remove(1L, url);

        verify(jdbcLinkDao, times(1)).delete(1L, url);
        verify(linkMapper, times(1)).toDto(link);
        assert response.equals(expectedResponse);
    }

    @Test
    void listAllCallsDaoFindAllAndMapperCorrectly() {
        List<Link> links = Collections.singletonList(new Link());
        List<LinkResponse> expectedResponses =
            Collections.singletonList(new LinkResponse(1L, URI.create("http://example.com")));

        when(jdbcLinkDao.findAll(anyLong())).thenReturn(links);
        when(linkMapper.toDto(any(Link.class))).thenReturn(expectedResponses.getFirst());

        List<LinkResponse> responses = linkService.listAll(1L);

        verify(jdbcLinkDao, times(1)).findAll(1L);
        verify(linkMapper, times(links.size())).toDto(any(Link.class));
        assert responses.equals(expectedResponses);
    }

    @Test
    void findLinksToCheckCallsDaoFindAllWithCorrectArguments() {
        OffsetDateTime lastCheckedBefore = OffsetDateTime.now().minusDays(1);
        List<Link> expectedLinks = Collections.singletonList(new Link());
        when(jdbcLinkDao.findAll(lastCheckedBefore)).thenReturn(expectedLinks);

        List<Link> actualLinks = linkService.findLinksToCheck(lastCheckedBefore);

        verify(jdbcLinkDao, times(1)).findAll(lastCheckedBefore);
        assertEquals(expectedLinks, actualLinks);
    }

    @Test
    void updateLinkCallsDaoUpdateWithCorrectLink() {
        Link linkToUpdate = new Link();
        linkToUpdate.setId(1L);
        linkToUpdate.setUrl("http://updated-example.com");
        linkToUpdate.setResourceType(ResourceType.GITHUB);
        linkToUpdate.setUpdatedAt(OffsetDateTime.now());
        linkToUpdate.setLastCheck(OffsetDateTime.now());

        doNothing().when(jdbcLinkDao).update(linkToUpdate);

        linkService.updateLink(linkToUpdate);

        verify(jdbcLinkDao, times(1)).update(linkToUpdate);
    }
}
