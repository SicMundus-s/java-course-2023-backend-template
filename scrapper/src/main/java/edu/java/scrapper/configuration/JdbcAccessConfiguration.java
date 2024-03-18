package edu.java.scrapper.configuration;

import edu.java.scrapper.dao.jdbc.JdbcChatDao;
import edu.java.scrapper.dao.jdbc.JdbcLinkDao;
import edu.java.scrapper.mapper.LinkMapper;
import edu.java.scrapper.service.ChatService;
import edu.java.scrapper.service.LinkService;
import edu.java.scrapper.service.impl.jdbc.JdbcChatServiceImpl;
import edu.java.scrapper.service.impl.jdbc.JdbcLinkServiceImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "appsrap", name = "database-access-type", havingValue = "jdbc")
public class JdbcAccessConfiguration {

    @Bean
    public ChatService jdbcChatService(JdbcChatDao jdbcChatDao) {
        return new JdbcChatServiceImpl(jdbcChatDao);
    }

    @Bean
    public LinkService jdbcLinkService(JdbcLinkDao jdbcLinkDao, LinkMapper linkMapper) {
        return new JdbcLinkServiceImpl(jdbcLinkDao, linkMapper);
    }
}
