package com.htmake.htbot.domain.profession.exception;

import com.htmake.htbot.global.error.BasicException;
import com.htmake.htbot.global.error.ErrorCode;

public class NotFoundJobException extends BasicException {
    public NotFoundJobException() {
        super(ErrorCode.NOT_FOUND_JOB);
    }
}
