package com.htmake.htbot.domain.skill.exception;

import com.htmake.htbot.global.error.BasicException;
import com.htmake.htbot.global.error.ErrorCode;

public class SkillNoLongerRegisteredException extends BasicException {

    public SkillNoLongerRegisteredException() {
        super(ErrorCode.SKILL_NO_LONGER_REGISTERED);
    }
}
