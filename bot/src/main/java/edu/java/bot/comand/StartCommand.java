package edu.java.bot.comand;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.service.CommandService;
import edu.java.core.dto.ResponseChat;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import static edu.java.bot.util.CommandExceptionHandler.handleCustomException;

@Component
@RequiredArgsConstructor
public class StartCommand implements Command {

    private final CommandService commandService;

    @Override
    public String command() {
        return "/start";
    }

    @Override
    public String description() {
        return "Start the bot and get welcome message";
    }

    @Override
    public SendMessage handle(Update update) {
        Long chatId = update.message().chat().id();
        String responseMessage;

        try {
            ResponseChat responseChat = commandService.register(chatId).block();
            responseMessage = responseChat.response();
        } catch (Exception e) {
            responseMessage = handleCustomException(e, chatId);
        }

        return new SendMessage(chatId, responseMessage);
    }
}
