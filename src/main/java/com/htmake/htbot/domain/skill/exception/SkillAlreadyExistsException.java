package com.htmake.htbot.domain.skill.exception;

import com.htmake.htbot.global.error.BasicException;
import com.htmake.htbot.global.error.ErrorCode;

public class SkillAlreadyExistsException extends BasicException {

    public SkillAlreadyExistsException() {
        super(ErrorCode.SKILL_ALREADY_EXISTS);
    }
}
