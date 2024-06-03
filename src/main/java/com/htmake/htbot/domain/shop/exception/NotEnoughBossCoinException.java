package com.htmake.htbot.domain.shop.exception;

import com.htmake.htbot.global.error.BasicException;
import com.htmake.htbot.global.error.ErrorCode;

public class NotEnoughBossCoinException extends BasicException {
    public NotEnoughBossCoinException() {
        super(ErrorCode.NOT_ENOUGH_BOSS_COIN);
    }
}
