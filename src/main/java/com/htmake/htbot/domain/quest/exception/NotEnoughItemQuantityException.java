package com.htmake.htbot.domain.quest.exception;

import com.htmake.htbot.global.error.BasicException;
import com.htmake.htbot.global.error.ErrorCode;

public class NotEnoughItemQuantityException extends BasicException {
    public NotEnoughItemQuantityException() {
        super(ErrorCode.NOT_ENOUGH_ITEM_QUANTITY);
    }
}
