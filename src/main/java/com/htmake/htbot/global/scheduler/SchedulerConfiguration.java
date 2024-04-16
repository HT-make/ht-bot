package com.htmake.htbot.global.scheduler;

import com.htmake.htbot.domain.shop.service.RandomShopItemShuffleService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SchedulerConfiguration {
    private final RandomShopItemShuffleService randomShopItemShuffleService;

    @Scheduled(cron = "0 0 * * * *")
    public void run() {
        randomShopItemShuffleService.execute();
    }
}
