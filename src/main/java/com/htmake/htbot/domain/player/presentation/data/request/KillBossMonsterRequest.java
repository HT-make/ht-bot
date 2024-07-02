package com.htmake.htbot.domain.player.presentation.data.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class KillBossMonsterRequest {

    @NotEmpty(message = "경험치는 필수 입니다.")
    private int exp;

    @NotEmpty(message = "골드는 필수 입니다.")
    private int gold;

    @NotEmpty(message = "젬은 필수 입니다.")
    private int gem;

    @NotEmpty(message = "보스 코인은 필수 입니다.")
    private int bossCoin;
}