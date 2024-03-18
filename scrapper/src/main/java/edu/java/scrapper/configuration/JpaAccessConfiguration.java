package edu.java.scrapper.configuration;

import edu.java.scrapper.dao.jpa.JpaChatDao;
import edu.java.scrapper.dao.jpa.JpaLinkDao;
import edu.java.scrapper.mapper.LinkMapper;
import edu.java.scrapper.service.ChatService;
import edu.java.scrapper.service.LinkService;
import edu.java.scrapper.service.impl.jpa.JpaChatServiceImpl;
import edu.java.scrapper.service.impl.jpa.JpaLinkServiceImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "appsrap", name = "database-access-type", havingValue = "jpa")
public class JpaAccessConfiguration {

    @Bean
    public ChatService jpaChatService(JpaChatDao jpaChatDao) {
        return new JpaChatServiceImpl(jpaChatDao);
    }

    @Bean
    public LinkService jpaLinkService(
        JpaLinkDao jpaLinkDao,
        JpaChatDao jpaChatDao,
        LinkMapper linkMapper
    ) {
        return new JpaLinkServiceImpl(jpaLinkDao, jpaChatDao, linkMapper);
    }
}
