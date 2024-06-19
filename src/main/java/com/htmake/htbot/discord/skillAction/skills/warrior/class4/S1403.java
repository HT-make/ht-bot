package com.htmake.htbot.discord.skillAction.skills.warrior.class4;

import com.htmake.htbot.discord.commands.battle.data.MonsterData;
import com.htmake.htbot.discord.commands.battle.data.PlayerData;
import com.htmake.htbot.discord.commands.battle.data.status.extend.PlayerOriginalStatus;
import com.htmake.htbot.discord.commands.battle.data.status.extend.PlayerStatus;
import com.htmake.htbot.discord.skillAction.condition.Condition;
import com.htmake.htbot.discord.skillAction.condition.extend.Light;
import com.htmake.htbot.discord.skillAction.skill.impl.AbstractSkillStrategy;
import com.htmake.htbot.discord.skillAction.type.SkillType;
import kotlin.Pair;

import java.util.List;
import java.util.Map;

public class S1403 extends AbstractSkillStrategy {

    @Override
    protected int getManaCost() {
        return 100;
    }

    @Override
    protected void applySkill(PlayerData playerData, MonsterData monsterData, List<Pair<String, SkillType>> resultList) {
        PlayerStatus playerStatus = playerData.getPlayerStatus();
        PlayerOriginalStatus playerOriginalStatus = playerData.getPlayerOriginalStatus();
        Map<String, Condition> playerCondition = playerStatus.getConditionMap();

        int additionalHealing = 0;

        if (playerCondition instanceof Light light && light.getTurn() >= 2) {
            additionalHealing = playerStatus.getDefence() * 2;
        }

        int healing = (int) (playerStatus.getDamage() * 2.5) + additionalHealing;
        playerStatus.setHealth(Math.min(playerOriginalStatus.getHealth(), playerStatus.getHealth() + healing));

        resultList.add(new Pair<>(String.valueOf(healing), SkillType.HEAL));
    }
}
