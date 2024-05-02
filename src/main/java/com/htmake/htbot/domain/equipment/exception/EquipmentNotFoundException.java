package com.htmake.htbot.domain.equipment.exception;

import com.htmake.htbot.global.error.BasicException;
import com.htmake.htbot.global.error.ErrorCode;

public class EquipmentNotFoundException extends BasicException {

    public EquipmentNotFoundException() {
        super(ErrorCode.EQUIPMENT_NOT_FOUND);
    }
}
