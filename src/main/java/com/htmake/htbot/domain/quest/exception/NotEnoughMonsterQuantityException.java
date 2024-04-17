package com.htmake.htbot.domain.quest.exception;

import com.htmake.htbot.global.error.BasicException;
import com.htmake.htbot.global.error.ErrorCode;

public class NotEnoughMonsterQuantityException extends BasicException {
    public NotEnoughMonsterQuantityException() {
        super(ErrorCode.NOT_ENOUGH_MONSTER_QUANTITY);
    }
}
