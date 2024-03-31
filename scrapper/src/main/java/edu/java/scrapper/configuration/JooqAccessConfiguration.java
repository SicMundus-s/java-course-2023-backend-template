package edu.java.scrapper.configuration;

import edu.java.scrapper.dao.jooq.JooqChatDao;
import edu.java.scrapper.dao.jooq.JooqLinkDao;
import edu.java.scrapper.mapper.LinkMapper;
import edu.java.scrapper.service.ChatService;
import edu.java.scrapper.service.LinkService;
import edu.java.scrapper.service.impl.jooq.JooqChatServiceImpl;
import edu.java.scrapper.service.impl.jooq.JooqLinkServiceImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "appsrap", name = "database-access-type", havingValue = "jooq")
public class JooqAccessConfiguration {

    @Bean
    public ChatService jooqChatService(JooqChatDao jooqChatDao) {
        return new JooqChatServiceImpl(jooqChatDao);
    }

    @Bean
    public LinkService jooqLinkService(JooqLinkDao jooqLinkDao, LinkMapper linkMapper) {
        return new JooqLinkServiceImpl(jooqLinkDao, linkMapper);
    }
}
