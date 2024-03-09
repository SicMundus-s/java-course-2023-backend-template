package edu.java.scrapper.service;

import edu.java.scrapper.client.StackOverflowClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StackOverflowService {

    private final StackOverflowClient stackOverflowClient;

    public void checkUpdate() {
        // ToDo Будущий сервис для StackOverflow
        //stackOverflowClient.fetchQuestionInfo()
    }
}
