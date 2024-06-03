package com.htmake.htbot.domain.shop.exception;

import com.htmake.htbot.global.error.BasicException;
import com.htmake.htbot.global.error.ErrorCode;

public class NotFoundBossShopException extends BasicException {

    public NotFoundBossShopException() {
        super(ErrorCode.NOT_FOUND_BOSS_SHOP);
    }
}
