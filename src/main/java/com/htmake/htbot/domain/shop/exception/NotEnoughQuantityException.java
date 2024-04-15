package com.htmake.htbot.domain.shop.exception;

import com.htmake.htbot.global.error.BasicException;
import com.htmake.htbot.global.error.ErrorCode;

public class NotEnoughQuantityException extends BasicException {

    public NotEnoughQuantityException() {
        super(ErrorCode.NOT_ENOUGH_QUANTITY);
    }
}
