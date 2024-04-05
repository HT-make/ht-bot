package com.htmake.htbot.domain.player.presentation.data.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PlayerJoinRequest {

    @NotBlank(message = "id는 필수 입니다.")
    private String userId;

    @NotBlank(message = "이름은 필수 입니다.")
    private String name;

    @NotBlank(message = "직업은 필수 입니다.")
    private String job;
}
