package com.htmake.htbot.discord.skillAction.skills.warrior.class1;

import com.htmake.htbot.discord.commands.battle.data.MonsterData;
import com.htmake.htbot.discord.commands.battle.data.PlayerData;
import com.htmake.htbot.discord.commands.battle.data.status.extend.PlayerOriginalStatus;
import com.htmake.htbot.discord.commands.battle.data.status.extend.PlayerStatus;
import com.htmake.htbot.discord.skillAction.condition.Condition;
import com.htmake.htbot.discord.skillAction.condition.extend.buff.extend.Power;
import com.htmake.htbot.discord.skillAction.skill.impl.AbstractSkillStrategy;
import com.htmake.htbot.discord.skillAction.type.SkillType;
import kotlin.Pair;

import java.util.List;
import java.util.Map;

public class S1105 extends AbstractSkillStrategy {

    @Override
    protected int getManaCost() {
        return 15;
    }

    @Override
    protected void applySkill(PlayerData playerData, MonsterData monsterData, List<Pair<String, SkillType>> resultList) {
        PlayerStatus playerStatus = playerData.getPlayerStatus();
        PlayerOriginalStatus playerOriginalStatus = playerData.getPlayerOriginalStatus();
        Map<String, Condition> playerCondition = playerStatus.getConditionMap();

        Power power = new Power(1);
        buffCheck(power.getId(), playerStatus, playerOriginalStatus, playerCondition);
        playerCondition.put(power.getId(), power);
        power.apply(playerStatus, playerOriginalStatus);

        resultList.add(new Pair<>(power.getName(), SkillType.BUFF));
    }
}
