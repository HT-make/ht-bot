package com.htmake.htbot.global.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    NOT_ENOUGH_GOLD("골드가 충분하지 않습니다.", 400),
    NOT_ENOUGH_QUANTITY("수량이 충분하지 않습니다.", 400),
    NOT_FOUND_ITEM("해당 아이템을 찾을 수 없습니다.", 404)
    ;
    private final String message;
    private final int status;
}
