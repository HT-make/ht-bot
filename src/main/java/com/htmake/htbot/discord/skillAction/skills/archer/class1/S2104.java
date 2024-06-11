package com.htmake.htbot.discord.skillAction.skills.archer.class1;

import com.htmake.htbot.discord.commands.battle.data.MonsterData;
import com.htmake.htbot.discord.commands.battle.data.PlayerData;
import com.htmake.htbot.discord.commands.battle.data.status.extend.PlayerOriginalStatus;
import com.htmake.htbot.discord.commands.battle.data.status.extend.PlayerStatus;
import com.htmake.htbot.discord.skillAction.skill.impl.AbstractSkillStrategy;
import com.htmake.htbot.discord.skillAction.type.SkillType;
import kotlin.Pair;

import java.util.List;

public class S2104 extends AbstractSkillStrategy {

    @Override
    protected int getManaCost() {
        return 20;
    }

    @Override
    protected void applySkill(PlayerData playerData, MonsterData monsterData, List<Pair<String, SkillType>> resultList) {
        PlayerStatus playerStatus = playerData.getPlayerStatus();
        PlayerOriginalStatus playerOriginalStatus = playerData.getPlayerOriginalStatus();

        int healing = (int) (playerStatus.getDamage() * 1.5);
        playerStatus.setHealth(Math.min(playerOriginalStatus.getHealth(), playerStatus.getHealth() + healing));

        resultList.add(new Pair<>(String.valueOf(healing), SkillType.HEAL));
    }
}
