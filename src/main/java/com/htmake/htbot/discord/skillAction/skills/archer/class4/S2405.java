package com.htmake.htbot.discord.skillAction.skills.archer.class4;

import com.htmake.htbot.discord.commands.battle.data.MonsterData;
import com.htmake.htbot.discord.commands.battle.data.PlayerData;
import com.htmake.htbot.discord.commands.battle.data.status.extend.PlayerOriginalStatus;
import com.htmake.htbot.discord.commands.battle.data.status.extend.PlayerStatus;
import com.htmake.htbot.discord.skillAction.condition.Condition;
import com.htmake.htbot.discord.skillAction.condition.extend.Buff;
import com.htmake.htbot.discord.skillAction.skill.impl.AbstractSkillStrategy;
import com.htmake.htbot.discord.skillAction.type.BuffStatus;
import com.htmake.htbot.discord.skillAction.type.BuffType;
import com.htmake.htbot.discord.skillAction.type.SkillType;
import kotlin.Pair;

import java.util.List;
import java.util.Map;

public class S2405 extends AbstractSkillStrategy {

    @Override
    protected int getManaCost() {
        return 60;
    }

    @Override
    protected void applySkill(PlayerData playerData, MonsterData monsterData, List<Pair<String, SkillType>> resultList) {
        PlayerStatus playerStatus = playerData.getPlayerStatus();
        PlayerOriginalStatus playerOriginalStatus = playerData.getPlayerOriginalStatus();
        Map<String, Condition> playerCondition = playerStatus.getConditionMap();

        Buff buff = new Buff(
                "power",
                "힘II",
                ":crossed_swords:",
                3,
                0.5,
                BuffType.UP,
                BuffStatus.DAMAGE
        );

        buffCheck("power", playerStatus, playerOriginalStatus, playerCondition);
        playerCondition.put("power", buff);
        buff.apply(playerStatus, playerOriginalStatus);

        resultList.add(new Pair<>("힘II", SkillType.BUFF));
    }
}
