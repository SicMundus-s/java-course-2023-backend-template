package edu.java.bot.handler;

import edu.java.core.entity.enums.ResourceType;
import org.springframework.stereotype.Component;

@Component
public class GitHubResourceHandler implements ResourceHandler {

    @Override
    public boolean canHandle(String resourceURL) {
        String gitHubRepoPattern = "https://github\\.com/([A-Za-z0-9_-]+)/([A-Za-z0-9_-]+)";
        return resourceURL.matches(gitHubRepoPattern);
    }

    @Override
    public ResourceType getResourceType() {
        return ResourceType.GITHUB;
    }
}
