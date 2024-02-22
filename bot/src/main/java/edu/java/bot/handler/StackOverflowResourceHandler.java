package edu.java.bot.handler;

import org.springframework.stereotype.Component;

@Component
public class StackOverflowResourceHandler implements ResourceHandler {

    @Override
    public boolean canHandle(String resourceURL) {
        String stackOverflowQuestionsPattern = "https:\\/\\/stackoverflow\\.com\\/questions\\/(\\d+)\\/[a-zA-Z0-9-]+";
        return resourceURL.contains(stackOverflowQuestionsPattern);
    }

    @Override
    public boolean checkUpdates(String resourceURL) {
        return true;
    }
}
