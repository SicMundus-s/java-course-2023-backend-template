package edu.java.scrapper.service.impl;

import edu.java.scrapper.client.GitHubClient;
import edu.java.scrapper.dto.GitHubRepositoryResponse;
import edu.java.scrapper.entity.Chat;
import edu.java.scrapper.entity.Link;
import edu.java.scrapper.mapper.LinkMapper;
import edu.java.scrapper.service.ChatService;
import edu.java.scrapper.service.LinkService;
import edu.java.scrapper.service.NotificationService;
import edu.java.scrapper.service.ResourceUpdateService;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class GitHubService implements ResourceUpdateService {

    private final GitHubClient gitHubClient;
    private final NotificationService notificationService;
    private final ChatService jdbcChatServiceImpl;
    private final LinkService jdbcLinkServiceImpl;
    private final LinkMapper linkMapper;

    @Override
    public void update(Link link) {
        Pattern pattern = Pattern.compile("https://github\\.com/([^/]+)/([^/]+)");
        Matcher matcher = pattern.matcher(link.getUrl());

        if (matcher.find()) {
            String owner = matcher.group(1);
            String repo = matcher.group(2);
            Mono<GitHubRepositoryResponse> gitHubResponse = gitHubClient.fetchRepositoryInfo(owner, repo);

            gitHubResponse.subscribe(response -> {
                OffsetDateTime gitHubPushedAt = response.pushedAt();
                OffsetDateTime gitHubUpdatedAt = response.updatedAt();
                boolean shouldNotify = false;
                StringBuilder description = new StringBuilder();

                if (link.getUpdatedAt().isBefore(gitHubPushedAt)) {
                    description.append("There was a new commit in repository ").append("\n");
                    link.setUpdatedAt(gitHubPushedAt);
                    shouldNotify = true;
                }

                if (link.getGithubUpdatedAt().isBefore(gitHubUpdatedAt)) {
                    description.append("The repository has been updated").append("\n");
                    link.setGithubUpdatedAt(gitHubUpdatedAt);
                    shouldNotify = true;
                }

                if (shouldNotify) {
                    List<Long> tgChatIds = jdbcChatServiceImpl.findAllChatsByLinkId(link.getId()).stream()
                        .map(Chat::getChatId)
                        .toList();
                    link.setLastCheck(OffsetDateTime.now());
                    jdbcLinkServiceImpl.updateLink(link);
                    notificationService
                        .sendNotification(linkMapper.toDtoUpdate(link, tgChatIds, description.toString()))
                        .subscribe(r -> log.info("Message sent: {}, update success", r.description()));
                } else {
                    link.setLastCheck(OffsetDateTime.now());
                    jdbcLinkServiceImpl.updateLink(link);
                }
            });

        }

    }
}
