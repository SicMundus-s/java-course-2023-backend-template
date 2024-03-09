package edu.java.scrapper.sheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class LinkUpdaterScheduler {

    @Scheduled(fixedDelayString = "#{@scheduler.interval()}")
    public void update() {
        log.info("Updating links...");
    }
}
