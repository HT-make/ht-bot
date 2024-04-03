package com.htmake.htbot.domain.player.presentation.data.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class KillMonsterRequest {

    @NotEmpty(message = "경험치는 필수 입니다.")
    private String exp;

    @NotEmpty(message = "골드는 필수 입니다.")
    private String gold;
}
