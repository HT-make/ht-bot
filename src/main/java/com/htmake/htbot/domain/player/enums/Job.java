package com.htmake.htbot.domain.player.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Job {

    WARRIOR("초급 전사"),
    SKILLED_WARRIOR("중급 전사"),
    HOLY_KNIGHT("성기사"),
    SWORD_MASTER("소드마스터"),

    ARCHER("초급 궁수"),
    SKILLED_ARCHER("중급 궁수"),
    SNIPER("저격수"),
    SPIRIT_ARCHER("스피릿 아처"),

    WIZARD("초급 마법사"),
    SKILLED_WIZARD("중급 마법사"),
    BLACK_WIZARD("흑마법사"),
    GREAT_WIZARD("대마법사");

    private final String name;

    public static Job toEnum(String job) {
        return Job.valueOf(job);
    }
}
