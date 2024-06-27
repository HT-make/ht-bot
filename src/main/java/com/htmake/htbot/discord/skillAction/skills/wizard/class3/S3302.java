package com.htmake.htbot.discord.skillAction.skills.wizard.class3;

import com.htmake.htbot.discord.commands.battle.data.MonsterData;
import com.htmake.htbot.discord.commands.battle.data.PlayerData;
import com.htmake.htbot.discord.commands.battle.data.status.extend.MonsterStatus;
import com.htmake.htbot.discord.commands.battle.data.status.extend.PlayerStatus;
import com.htmake.htbot.discord.skillAction.skill.action.SkillAction;
import com.htmake.htbot.discord.skillAction.skill.impl.AbstractSkillStrategy;
import com.htmake.htbot.discord.skillAction.type.SkillType;
import kotlin.Pair;

import java.util.List;

public class S3302 extends AbstractSkillStrategy {

    @Override
    protected int getManaCost() {
        return 100;
    }

    @Override
    protected void applySkill(PlayerData playerData, MonsterData monsterData, List<Pair<String, SkillType>> resultList) {
        PlayerStatus playerStatus = playerData.getPlayerStatus();
        MonsterStatus monsterStatus = monsterData.getMonsterStatus();

        SkillAction skillAction = new SkillAction();
        skillAction.attack(1.1, playerStatus, monsterStatus, resultList);
        skillAction.attack(1.6, playerStatus, monsterStatus, resultList);
        skillAction.attack(2.3, playerStatus, monsterStatus, resultList);
    }
}
