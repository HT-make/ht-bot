package com.htmake.htbot.discord.skillAction.skills.archer.class3;

import com.htmake.htbot.discord.commands.battle.data.MonsterData;
import com.htmake.htbot.discord.commands.battle.data.PlayerData;
import com.htmake.htbot.discord.skillAction.condition.Condition;
import com.htmake.htbot.discord.skillAction.condition.extend.Buff;
import com.htmake.htbot.discord.commands.battle.data.status.extend.PlayerOriginalStatus;
import com.htmake.htbot.discord.commands.battle.data.status.extend.PlayerStatus;
import com.htmake.htbot.discord.skillAction.skill.impl.AbstractSkillStrategy;
import com.htmake.htbot.discord.skillAction.type.BuffStatus;
import com.htmake.htbot.discord.skillAction.type.BuffType;
import com.htmake.htbot.discord.skillAction.type.SkillType;
import kotlin.Pair;

import java.util.List;
import java.util.Map;

public class S2305 extends AbstractSkillStrategy {

    @Override
    protected int getManaCost() {
        return 80;
    }

    @Override
    protected void applySkill(PlayerData playerData, MonsterData monsterData, List<Pair<String, SkillType>> resultList) {
        PlayerStatus playerStatus = playerData.getPlayerStatus();
        PlayerOriginalStatus playerOriginalStatus = playerData.getPlayerOriginalStatus();
        Map<String, Condition> playerCondition = playerStatus.getConditionMap();

        Buff buff = new Buff(
                "hit",
                "명중III",
                ":dart:",
                3,
                0.4,
                BuffType.UP,
                BuffStatus.CRITICAL_CHANCE
        );

        buffCheck("hit", playerStatus, playerOriginalStatus, playerCondition);
        playerCondition.put("hit", buff);
        buff.apply(playerStatus, playerOriginalStatus);

        resultList.add(new Pair<>("명중III", SkillType.BUFF));
    }
}
