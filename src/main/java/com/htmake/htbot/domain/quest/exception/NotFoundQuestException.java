package com.htmake.htbot.domain.quest.exception;

import com.htmake.htbot.global.error.BasicException;
import com.htmake.htbot.global.error.ErrorCode;

public class NotFoundQuestException extends BasicException {

    public NotFoundQuestException() {
        super(ErrorCode.NOT_FOUND_QUEST);
    }
}
