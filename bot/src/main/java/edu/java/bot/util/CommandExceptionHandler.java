package edu.java.bot.util;

import edu.java.bot.exception.UrlNotSupported;
import edu.java.core.exception.BadRequestException;
import edu.java.core.exception.NotFoundException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CommandExceptionHandler {
    private CommandExceptionHandler() {

    }

    public static String handleCustomException(Exception e, Long id) {
        log.error("{} id: {}", e.getMessage(), id);
        return switch (e) {
            case UrlNotSupported urlNotSupported -> urlNotSupported.getMessage();
            case BadRequestException badRequestException -> badRequestException.getMessage();
            case NotFoundException notFoundException -> notFoundException.getMessage();
            default -> "Something went wrong. Please try again later.";
        };
    }
}
