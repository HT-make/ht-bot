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

    //QUEST
    NOT_FOUND_QUEST("퀘스트를 찾을 수 없습니다.", 404),
    NOT_ENOUGH_MONSTER_QUANTITY("몬스터 수량이 부족합니다.", 400),
    NOT_ENOUGH_ITEM_QUANTITY("아이템 수량이 부족합니다.", 400),

    //DICT
    NOT_FOUND_NAME("해당 이름을 찾을 수 없습니다.", 404),
    NOT_FOUND_CATEGORY("해당 카테고리를 찾을 수 없습니다.", 404);
    private final String message;
    private final int status;
}
