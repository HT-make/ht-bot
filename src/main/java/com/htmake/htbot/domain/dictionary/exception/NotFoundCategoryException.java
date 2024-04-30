package com.htmake.htbot.domain.dictionary.exception;

import com.htmake.htbot.global.error.BasicException;
import com.htmake.htbot.global.error.ErrorCode;

public class NotFoundCategoryException extends BasicException {
    public NotFoundCategoryException() {
        super(ErrorCode.NOT_FOUND_CATEGORY);
    }
}
