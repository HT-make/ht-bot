package com.htmake.htbot.domain.shop.exception;

import com.htmake.htbot.global.error.BasicException;
import com.htmake.htbot.global.error.ErrorCode;

public class NotFoundItemException extends BasicException {

    public NotFoundItemException() {
        super(ErrorCode.NOT_FOUND_ITEM);
    }
}
