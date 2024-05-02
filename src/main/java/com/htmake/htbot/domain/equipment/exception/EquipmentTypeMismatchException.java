package com.htmake.htbot.domain.equipment.exception;

import com.htmake.htbot.global.error.BasicException;
import com.htmake.htbot.global.error.ErrorCode;

public class EquipmentTypeMismatchException extends BasicException {

    public EquipmentTypeMismatchException() {
        super(ErrorCode.EQUIPMENT_TYPE_MISMATCH);
    }
}
