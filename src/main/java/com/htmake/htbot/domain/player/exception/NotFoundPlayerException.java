package com.htmake.htbot.domain.player.exception;

import com.htmake.htbot.global.error.BasicException;
import com.htmake.htbot.global.error.ErrorCode;

public class NotFoundPlayerException extends BasicException {

    public NotFoundPlayerException() {
        super(ErrorCode.NOT_FOUND_PLAYER);
    }
}
