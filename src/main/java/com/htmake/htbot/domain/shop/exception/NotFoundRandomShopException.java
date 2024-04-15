package com.htmake.htbot.domain.shop.exception;

import com.htmake.htbot.global.error.BasicException;
import com.htmake.htbot.global.error.ErrorCode;

public class NotFoundRandomShopException extends BasicException {

    public NotFoundRandomShopException() {
        super(ErrorCode.NOT_FOUND_RANDOM_SHOP);
    }
}
