package edu.java.bot.comand;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.service.CommandService;
import edu.java.core.dto.LinkResponse;
import edu.java.core.dto.RemoveLinkRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import static edu.java.bot.util.CommandExceptionHandler.handleCustomException;

@Component
@RequiredArgsConstructor
public class UntrackCommand implements Command {
    private final CommandService commandService;

    @Override
    public String command() {
        return "/untrack";
    }

    @Override
    public String description() {
        return "Stop tracking url";
    }

    @Override
    public SendMessage handle(Update update) {
        Long id = update.message().chat().id();
        String messageText = update.message().text();
        String[] parts = messageText.split("\\s+", 2);

        if (parts.length < 2) {
            return new SendMessage(id, "Usage: /untrack <URL>");
        }

        String url = parts[1];
        String response;
        try {
            LinkResponse linkResponse = commandService.removeLink(id, new RemoveLinkRequest(url)).block();
            response = "Untracking: " + linkResponse.url().toString();
        } catch (Exception e) {
            response = handleCustomException(e, id);
        }

        return new SendMessage(id, response);
    }
}
