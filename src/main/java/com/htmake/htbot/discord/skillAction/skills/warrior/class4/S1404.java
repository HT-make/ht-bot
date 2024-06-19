package com.htmake.htbot.discord.skillAction.skills.warrior.class4;

import com.htmake.htbot.discord.commands.battle.data.MonsterData;
import com.htmake.htbot.discord.commands.battle.data.PlayerData;
import com.htmake.htbot.discord.commands.battle.data.status.extend.PlayerOriginalStatus;
import com.htmake.htbot.discord.commands.battle.data.status.extend.PlayerStatus;
import com.htmake.htbot.discord.skillAction.condition.Condition;
import com.htmake.htbot.discord.skillAction.condition.extend.Thorn;
import com.htmake.htbot.discord.skillAction.condition.extend.buff.extend.Absorption;
import com.htmake.htbot.discord.skillAction.skill.impl.AbstractSkillStrategy;
import com.htmake.htbot.discord.skillAction.type.SkillType;
import kotlin.Pair;

import java.util.List;
import java.util.Map;

public class S1404 extends AbstractSkillStrategy {

    @Override
    protected int getManaCost() {
        return 60;
    }

    @Override
    protected void applySkill(PlayerData playerData, MonsterData monsterData, List<Pair<String, SkillType>> resultList) {
        PlayerStatus playerStatus = playerData.getPlayerStatus();
        PlayerOriginalStatus playerOriginalStatus = playerData.getPlayerOriginalStatus();
        Map<String, Condition> playerCondition = playerStatus.getConditionMap();

        Absorption absorption = new Absorption(1);
        buffCheck(absorption.getId(), playerStatus, playerOriginalStatus, playerCondition);
        playerCondition.put(absorption.getId(), absorption);
        absorption.apply(playerStatus, playerOriginalStatus);

        Thorn thorn = new Thorn(1);
        playerCondition.put(thorn.getId(), thorn);

        resultList.add(new Pair<>(absorption.getName(), SkillType.BUFF));
        resultList.add(new Pair<>(thorn.getName(), SkillType.BUFF));
    }
}
