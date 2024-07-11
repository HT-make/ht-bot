package com.htmake.htbot.discord.skillAction.skills.wizard.class4;

import com.htmake.htbot.discord.commands.battle.data.MonsterData;
import com.htmake.htbot.discord.commands.battle.data.PlayerData;
import com.htmake.htbot.discord.commands.battle.data.status.extend.PlayerOriginalStatus;
import com.htmake.htbot.discord.commands.battle.data.status.extend.PlayerStatus;
import com.htmake.htbot.discord.skillAction.skill.action.SkillAction;
import com.htmake.htbot.discord.skillAction.skill.impl.AbstractSkillStrategy;
import com.htmake.htbot.discord.skillAction.type.SkillType;
import kotlin.Pair;

import java.util.List;

public class S3402 extends AbstractSkillStrategy {

    @Override
    protected int getManaCost() {
        return 120;
    }

    @Override
    protected void applySkill(PlayerData playerData, MonsterData monsterData, List<Pair<String, SkillType>> resultList) {
        PlayerStatus playerStatus = playerData.getPlayerStatus();
        PlayerOriginalStatus playerOriginalStatus = playerData.getPlayerOriginalStatus();

        SkillAction skillAction = new SkillAction();
        skillAction.attack(6.0, playerData, monsterData, resultList);

        if (skillAction.isCritical()) {
            int healing = (int) (skillAction.getDamageReceived() * 0.7);
            int maxHealth = playerOriginalStatus.getHealth();
            playerStatus.setHealth(Math.max(maxHealth, playerStatus.getHealth() + healing));
        } else {
            int damage = (int) (skillAction.getDamageReceived() * 0.3);
            playerStatus.setHealth(Math.max(0, playerStatus.getHealth()) - damage);
        }
    }
}
