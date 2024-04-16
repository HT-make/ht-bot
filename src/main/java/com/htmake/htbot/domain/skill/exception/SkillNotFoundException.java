package com.htmake.htbot.domain.skill.exception;

import com.htmake.htbot.global.error.BasicException;
import com.htmake.htbot.global.error.ErrorCode;

public class SkillNotFoundException extends BasicException {

    public SkillNotFoundException() {
        super(ErrorCode.SKILL_NOT_FOUND);
    }
}
