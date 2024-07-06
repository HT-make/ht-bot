package com.htmake.htbot.domain.quest.exception;

import com.htmake.htbot.global.error.BasicException;
import com.htmake.htbot.global.error.ErrorCode;

public class NotFoundPlayerTargetMonsterException extends BasicException {

    public NotFoundPlayerTargetMonsterException() {
        super(ErrorCode.NOT_FOUND_PLAYER_TARGET_MONSTER);
    }
}
