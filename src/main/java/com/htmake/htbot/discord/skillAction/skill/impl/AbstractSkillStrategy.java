package com.htmake.htbot.discord.skillAction.skill.impl;

import com.htmake.htbot.discord.commands.battle.data.MonsterData;
import com.htmake.htbot.discord.commands.battle.data.PlayerData;
import com.htmake.htbot.discord.commands.battle.data.status.extend.PlayerStatus;
import com.htmake.htbot.discord.skillAction.skill.SkillStrategy;
import com.htmake.htbot.discord.skillAction.type.SkillType;
import kotlin.Pair;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractSkillStrategy implements SkillStrategy {

    protected abstract int getManaCost();

    protected abstract void applySkill(PlayerData playerData, MonsterData monsterData, List<Pair<String, SkillType>> resultList);

    @Override
    public List<Pair<String, SkillType>> execute(PlayerData playerData, MonsterData monsterData) {
        List<Pair<String, SkillType>> resultList = new ArrayList<>();

        PlayerStatus playerStatus = playerData.getPlayerStatus();

        if (manaCheck(playerStatus, getManaCost(), resultList)) {
            return resultList;
        }

        applySkill(playerData, monsterData, resultList);

        return resultList;
    }
}
