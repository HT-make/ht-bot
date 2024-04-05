package com.htmake.htbot.domain.player.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Job {

    WARRIOR("전사"),
    ARCHER("궁수"),
    WIZARD("마법사");

    private final String name;

    public static Job toEnum(String job) {
        return Job.valueOf(job);
    }
}
