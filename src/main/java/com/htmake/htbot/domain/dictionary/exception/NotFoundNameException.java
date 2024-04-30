package com.htmake.htbot.domain.dictionary.exception;

import com.htmake.htbot.global.error.BasicException;
import com.htmake.htbot.global.error.ErrorCode;

public class NotFoundNameException extends BasicException {
    public NotFoundNameException() {
        super(ErrorCode.NOT_FOUND_NAME);
    }
}
