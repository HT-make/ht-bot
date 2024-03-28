package com.htmake.htbot.domain.player.presentation.data.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PlayerInfoResponse {

    private int level;

    private int currentExp;

    private int maxExp;
}
