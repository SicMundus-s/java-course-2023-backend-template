package edu.java.bot.handler;

import edu.java.core.entity.enums.ResourceType;
import org.springframework.stereotype.Component;

@Component
public class StackOverflowResourceHandler implements ResourceHandler {

    @Override
    public boolean canHandle(String resourceURL) {
        String stackOverflowQuestionsPattern =
            "https?:\\/\\/([a-zA-Z0-9]+\\.)*(stackoverflow|stackexchange|superuser|askubuntu)"
                + "\\.com\\/questions\\/(\\d+)\\/?.*";
        return resourceURL.matches(stackOverflowQuestionsPattern);
    }

    @Override
    public ResourceType getResourceType() {
        return ResourceType.STACKOVERFLOW;
    }
}
