package com.htmake.htbot.domain.inventory.exception;

import com.htmake.htbot.global.error.BasicException;
import com.htmake.htbot.global.error.ErrorCode;

public class InventoryItemNotFoundException extends BasicException {

    public InventoryItemNotFoundException() {
        super(ErrorCode.INVENTORY_ITEM_NOT_FOUND);
    }
}
