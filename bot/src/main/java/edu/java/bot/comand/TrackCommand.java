package edu.java.bot.comand;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.service.CommandService;
import edu.java.core.dto.AddLinkRequest;
import edu.java.core.dto.LinkResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import static edu.java.bot.util.CommandExceptionHandler.handleCustomException;

@Component
@RequiredArgsConstructor
@Slf4j
public class TrackCommand implements Command {

    private final CommandService commandService;

    @Override
    public String command() {
        return "/track";
    }

    @Override
    public String description() {
        return "Start tracking URL";
    }

    @Override
    public SendMessage handle(Update update) {
        Long id = update.message().chat().id();
        String messageText = update.message().text();
        String[] parts = messageText.split("\\s+", 2);

        if (parts.length < 2) {
            return new SendMessage(id, "Usage: /track <URL>");
        }
        String url = parts[1];
        AddLinkRequest addLinkRequest = new AddLinkRequest();
        addLinkRequest.setLink(url);

        String response;
        try {
            LinkResponse linkResponse = commandService.addLink(id, addLinkRequest).block();
            response = "Started tracking: " + linkResponse.url().toString();
        } catch (Exception e) {
            response = handleCustomException(e, id);
        }
        return new SendMessage(id, response);
    }
}
