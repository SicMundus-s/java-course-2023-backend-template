package edu.java.bot.comand;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.service.CommandService;
import edu.java.core.dto.LinkResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import static edu.java.bot.util.CommandExceptionHandler.handleCustomException;

@Component
@RequiredArgsConstructor
public class ListCommand implements Command {

    private final CommandService commandService;

    @Override
    public String command() {
        return "/list";
    }

    @Override
    public String description() {
        return "Outputs a list of tracked links";
    }

    @Override
    public SendMessage handle(Update update) {
        Long chatId = update.message().chat().id();
        String responseMessage;
        try {
            List<LinkResponse> urls = commandService.getLinks(chatId).block().links();
            StringBuilder helpUrls = new StringBuilder();
            urls.stream().map(LinkResponse::url).forEach(u -> helpUrls.append(u).append("\n"));
            responseMessage = helpUrls.toString();
        } catch (Exception e) {
            responseMessage = handleCustomException(e, chatId);
        }

        return new SendMessage(chatId, responseMessage);
    }
}
