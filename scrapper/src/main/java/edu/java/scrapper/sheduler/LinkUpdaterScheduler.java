package edu.java.scrapper.sheduler;

import edu.java.core.entity.enums.ResourceType;
import edu.java.scrapper.entity.Link;
import edu.java.scrapper.service.LinkService;
import edu.java.scrapper.service.LinkUpdater;
import edu.java.scrapper.service.ResourceUpdateService;
import edu.java.scrapper.service.impl.GitHubService;
import edu.java.scrapper.service.impl.StackOverflowService;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class LinkUpdaterScheduler implements LinkUpdater {

    private final LinkService jdbcLinkServiceImpl;
    private final Map<ResourceType, ResourceUpdateService> resourceClientMap = new HashMap<>();
    private static final int DIFFERENT_CHECK_DAYS = 1;

    public LinkUpdaterScheduler(LinkService jdbcLinkServiceImpl, List<ResourceUpdateService> services) {
        this.jdbcLinkServiceImpl = jdbcLinkServiceImpl;
        services.forEach(service -> {
            if (service instanceof GitHubService) {
                resourceClientMap.put(ResourceType.GITHUB, service);
            } else if (service instanceof StackOverflowService) {
                resourceClientMap.put(ResourceType.STACKOVERFLOW, service);
            }
        });
    }

    @Scheduled(fixedDelayString = "#{@scheduler.interval()}")
    public void update() {
        List<Link> linksToCheck =
            jdbcLinkServiceImpl.findLinksToCheck(OffsetDateTime.now().minusDays(DIFFERENT_CHECK_DAYS));
        linksToCheck.forEach(link -> {
            ResourceUpdateService resourceUpdateService = resourceClientMap.get(link.getResourceType());
            resourceUpdateService.update(link);
        });
    }
}
