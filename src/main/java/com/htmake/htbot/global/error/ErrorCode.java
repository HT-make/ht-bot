package com.htmake.htbot.global.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    //PLAYER
    NOT_FOUND_PLAYER("플레이어를 찾을 수 없습니다.", 404),

    //SHOP
    NOT_ENOUGH_GOLD("골드가 충분하지 않습니다.", 400),
    NOT_ENOUGH_QUANTITY("매진된 상품입니다.", 400),
    NOT_FOUND_ITEM("해당 아이템을 찾을 수 없습니다.", 404),
    NOT_FOUND_RANDOM_SHOP("랜덤 상점을 찾을 수 없습니다.", 404),

    //SKILL
    SKILL_NOT_FOUND("스킬을 찾을 수 없습니다.", 404),
    SKILL_NO_LONGER_REGISTERED("더 이상 등록할 수 없습니다.", 409),
    SKILL_ALREADY_EXISTS("이미 등록중인 스킬입니다.", 409);

    private final String message;
    private final int status;
}
