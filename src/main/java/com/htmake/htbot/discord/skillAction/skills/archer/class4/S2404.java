package com.htmake.htbot.discord.skillAction.skills.archer.class4;

import com.htmake.htbot.discord.commands.battle.data.MonsterData;
import com.htmake.htbot.discord.commands.battle.data.PlayerData;
import com.htmake.htbot.discord.commands.battle.data.status.extend.PlayerStatus;
import com.htmake.htbot.discord.skillAction.condition.Condition;
import com.htmake.htbot.discord.skillAction.condition.extend.Invincible;
import com.htmake.htbot.discord.skillAction.skill.impl.AbstractSkillStrategy;
import com.htmake.htbot.discord.skillAction.type.SkillType;
import kotlin.Pair;

import java.util.List;
import java.util.Map;

public class S2404 extends AbstractSkillStrategy {

    @Override
    protected int getManaCost() {
        return 80;
    }

    @Override
    protected void applySkill(PlayerData playerData, MonsterData monsterData, List<Pair<String, SkillType>> resultList) {
        PlayerStatus playerStatus = playerData.getPlayerStatus();
        Map<String, Condition> playerCondition = playerStatus.getConditionMap();

        Invincible invincible = new Invincible(
                "invincible",
                "무적",
                ":star:",
                1
        );

        playerCondition.put("invincible", invincible);

        resultList.add(new Pair<>("무적", SkillType.BUFF));
    }
}
