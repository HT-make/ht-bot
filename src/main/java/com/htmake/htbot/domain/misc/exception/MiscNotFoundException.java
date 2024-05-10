package com.htmake.htbot.domain.misc.exception;

import com.htmake.htbot.global.error.BasicException;
import com.htmake.htbot.global.error.ErrorCode;

public class MiscNotFoundException extends BasicException {

    public MiscNotFoundException() {
        super(ErrorCode.MISC_NOT_FOUND);
    }
}
