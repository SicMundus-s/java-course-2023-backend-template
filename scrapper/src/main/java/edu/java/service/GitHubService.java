package edu.java.service;

import edu.java.client.GitHubClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GitHubService {

    private final GitHubClient gitHubClient;

    public void checkUpdate() {
        // ToDo Будущий сервис для Гитхаба
        //Mono<GitHubRepositoryResponse> gitHubRepositoryResponseMono = gitHubClient.fetchRepositoryInfo();
    }
}
