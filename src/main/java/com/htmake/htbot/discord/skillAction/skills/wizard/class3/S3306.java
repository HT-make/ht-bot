package com.htmake.htbot.discord.skillAction.skills.wizard.class3;

import com.htmake.htbot.discord.commands.battle.data.MonsterData;
import com.htmake.htbot.discord.commands.battle.data.PlayerData;
import com.htmake.htbot.discord.commands.battle.data.status.extend.PlayerOriginalStatus;
import com.htmake.htbot.discord.commands.battle.data.status.extend.PlayerStatus;
import com.htmake.htbot.discord.skillAction.skill.action.SkillAction;
import com.htmake.htbot.discord.skillAction.skill.impl.AbstractSkillStrategy;
import com.htmake.htbot.discord.skillAction.type.SkillType;
import kotlin.Pair;

import java.util.List;

public class S3306 extends AbstractSkillStrategy {

    @Override
    protected int getManaCost() {
        return 50;
    }

    @Override
    protected void applySkill(PlayerData playerData, MonsterData monsterData, List<Pair<String, SkillType>> resultList) {
        PlayerStatus playerStatus = playerData.getPlayerStatus();
        PlayerOriginalStatus playerOriginalStatus = playerData.getPlayerOriginalStatus();

        SkillAction skillAction = new SkillAction();
        int healing = (int) (playerStatus.getDamage() * 3.0);
        skillAction.heal(healing, playerStatus, playerOriginalStatus, resultList);
    }
}
