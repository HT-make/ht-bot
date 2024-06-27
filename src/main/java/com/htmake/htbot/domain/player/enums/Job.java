package com.htmake.htbot.domain.player.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Set;

@Getter
@RequiredArgsConstructor
public enum Job {

    WARRIOR("초급 전사", Set.of()),
    SKILLED_WARRIOR("중급 전사", Set.of(WARRIOR)),
    HOLY_KNIGHT("성기사", Set.of(WARRIOR, SKILLED_WARRIOR)),
    SWORD_MASTER("소드마스터", Set.of(WARRIOR, SKILLED_WARRIOR)),

    ARCHER("초급 궁수", Set.of()),
    SKILLED_ARCHER("중급 궁수", Set.of(ARCHER)),
    SNIPER("저격수", Set.of(ARCHER, SKILLED_ARCHER)),
    SPIRIT_ARCHER("스피릿 아처", Set.of(ARCHER, SKILLED_ARCHER)),

    WIZARD("초급 마법사", Set.of()),
    SKILLED_WIZARD("중급 마법사", Set.of(WIZARD)),
    BLACK_WIZARD("흑마법사", Set.of(WIZARD, SKILLED_WIZARD)),
    GREAT_WIZARD("대마법사", Set.of(WIZARD, SKILLED_WIZARD));

    private final String name;
    private final Set<Job> lowerJob;

    public static Job toEnum(String job) {
        return Job.valueOf(job);
    }
}
