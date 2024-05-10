package com.htmake.htbot.domain.dungeon.exception;

import com.htmake.htbot.global.error.BasicException;
import com.htmake.htbot.global.error.ErrorCode;

public class DungeonNotFoundException extends BasicException {

    public DungeonNotFoundException() {
        super(ErrorCode.DUNGEON_NOT_FOUND);
    }
}
