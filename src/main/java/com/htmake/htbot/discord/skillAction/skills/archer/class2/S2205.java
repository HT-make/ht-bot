package com.htmake.htbot.discord.skillAction.skills.archer.class2;

import com.htmake.htbot.discord.commands.battle.data.MonsterData;
import com.htmake.htbot.discord.commands.battle.data.PlayerData;
import com.htmake.htbot.discord.skillAction.condition.extend.Buff;
import com.htmake.htbot.discord.skillAction.condition.Condition;
import com.htmake.htbot.discord.commands.battle.data.status.extend.PlayerOriginalStatus;
import com.htmake.htbot.discord.commands.battle.data.status.extend.PlayerStatus;
import com.htmake.htbot.discord.skillAction.skills.SkillStrategy;
import com.htmake.htbot.discord.skillAction.type.BuffStatus;
import com.htmake.htbot.discord.skillAction.type.BuffType;
import com.htmake.htbot.discord.skillAction.type.SkillType;
import kotlin.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class S2205 implements SkillStrategy {

    @Override
    public List<Pair<String, SkillType>> execute(PlayerData playerData, MonsterData monsterData) {
        List<Pair<String, SkillType>> resultList = new ArrayList<>();

        PlayerStatus playerStatus = playerData.getPlayerStatus();
        PlayerOriginalStatus playerOriginalStatus = playerData.getPlayerOriginalStatus();

        Map<String, Condition> playerCondition = playerStatus.getConditionMap();
        Buff buff = new Buff(
                "hit",
                "명중I",
                ":dart:",
                3,
                0.15,
                BuffType.UP,
                BuffStatus.CRITICAL_CHANCE
        );

        buffCheck("hit", playerStatus, playerOriginalStatus, playerCondition);

        playerCondition.put("hit", buff);

        buff.apply(playerStatus, playerOriginalStatus);

        resultList.add(new Pair<>("명중I", SkillType.BUFF));

        return resultList;
    }
}
