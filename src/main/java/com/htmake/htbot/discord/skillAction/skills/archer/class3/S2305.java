package com.htmake.htbot.discord.skillAction.skills.archer.class3;

import com.htmake.htbot.discord.commands.battle.data.MonsterData;
import com.htmake.htbot.discord.commands.battle.data.PlayerData;
import com.htmake.htbot.discord.skillAction.condition.Condition;
import com.htmake.htbot.discord.commands.battle.data.status.extend.PlayerOriginalStatus;
import com.htmake.htbot.discord.commands.battle.data.status.extend.PlayerStatus;
import com.htmake.htbot.discord.skillAction.condition.extend.buff.extend.Hit;
import com.htmake.htbot.discord.skillAction.skill.impl.AbstractSkillStrategy;
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

        Hit hit = new Hit(2);
        buffCheck(hit.getId(), playerStatus, playerOriginalStatus, playerCondition);
        playerCondition.put(hit.getId(), hit);
        hit.apply(playerStatus, playerOriginalStatus);

        resultList.add(new Pair<>(hit.getName(), SkillType.BUFF));
    }
}
