package com.htmake.htbot.domain.player.presentation.data.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class KillMonsterRequest {

    @NotEmpty(message = "경험치는 필수 입니다.")
    private int exp;

    @NotEmpty(message = "골드는 필수 입니다.")
    private int gold;

    private List<GetDropItemRequest> getItemList;
}
