package edu.java.scrapper.service.impl;

import edu.java.scrapper.client.StackOverflowClient;
import edu.java.scrapper.entity.Chat;
import edu.java.scrapper.entity.Link;
import edu.java.scrapper.mapper.LinkMapper;
import edu.java.scrapper.service.ChatService;
import edu.java.scrapper.service.LinkService;
import edu.java.scrapper.service.NotificationService;
import edu.java.scrapper.service.ResourceUpdateService;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class StackOverflowService implements ResourceUpdateService {

    private final StackOverflowClient stackOverflowClient;
    private final ChatService jdbcChatServiceImpl;
    private final NotificationService notificationService;
    private final LinkMapper linkMapper;
    private final LinkService jdbcLinkServiceImpl;

    @Override
    public void update(Link link) {
        String regex =
            "https?://([a-zA-Z0-9]+\\.)*(stackoverflow|stackexchange|superuser|askubuntu)\\.com/questions/(\\d+)/?";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(link.getUrl());

        if (matcher.find()) {
            int group = 3;
            String questionId = matcher.group(group);

            stackOverflowClient.fetchQuestionInfo(questionId).subscribe(response -> {
                Long lastActivityDate = response.items().getFirst().lastActivityDate();
                Long lastEditDateQuestion = response.items().getFirst().lastEditDateQuestion();
                OffsetDateTime dateTimeLastActivity =
                    OffsetDateTime.ofInstant(Instant.ofEpochSecond(lastActivityDate), ZoneOffset.UTC);
                OffsetDateTime stackoverflowLastEditDateQuestion =
                    OffsetDateTime.ofInstant(Instant.ofEpochSecond(lastEditDateQuestion), ZoneOffset.UTC);
                boolean shouldNotify = false;
                StringBuilder description = new StringBuilder();

                if (link.getUpdatedAt().isBefore(dateTimeLastActivity)) {
                    description.append("The question has been updated").append("\n");
                    link.setUpdatedAt(stackoverflowLastEditDateQuestion);
                    shouldNotify = true;
                }

                if (link.getStackoverflowLastEditDateQuestion().isBefore(stackoverflowLastEditDateQuestion)) {
                    description.append("The question has been edited").append("\n");
                    link.setStackoverflowLastEditDateQuestion(stackoverflowLastEditDateQuestion);
                    shouldNotify = true;
                }

                if (shouldNotify) {
                    List<Long> tgChatIds = jdbcChatServiceImpl.findAllChatsByLinkId(link.getId()).stream()
                        .map(Chat::getChatId)
                        .toList();
                    link.setLastCheck(OffsetDateTime.now());
                    jdbcLinkServiceImpl.updateLink(link);
                    notificationService.sendNotification(linkMapper.toDtoUpdate(
                            link,
                            tgChatIds,
                            description.toString()
                        ))
                        .subscribe(r -> log.info("Message sent: {}, update success", r.description()));
                } else {
                    link.setLastCheck(OffsetDateTime.now());
                    jdbcLinkServiceImpl.updateLink(link);
                }
            });
        }
    }
}
