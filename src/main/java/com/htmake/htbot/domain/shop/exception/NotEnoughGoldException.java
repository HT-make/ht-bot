package com.htmake.htbot.domain.shop.exception;

import com.htmake.htbot.global.error.BasicException;
import com.htmake.htbot.global.error.ErrorCode;

public class NotEnoughGoldException extends BasicException {

    public NotEnoughGoldException() {
        super(ErrorCode.NOT_ENOUGH_GOLD);
    }
}
