package com.htmake.htbot.domain.inventory.exception;

import com.htmake.htbot.global.error.BasicException;
import com.htmake.htbot.global.error.ErrorCode;

public class InventoryIsMaxException extends BasicException {

    public InventoryIsMaxException() {
        super(ErrorCode.INVENTORY_IS_MAX);
    }
}
