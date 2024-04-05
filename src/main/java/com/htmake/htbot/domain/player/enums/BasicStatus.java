package com.htmake.htbot.domain.player.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BasicStatus {

    MANA(100),
    CRITICAL_CHANCE(10),
    CRITICAL_DAMAGE(200);

    private final int value;
}
