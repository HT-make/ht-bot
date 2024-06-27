package com.htmake.htbot.discord.skillAction.skills.wizard.class3;

import com.htmake.htbot.discord.commands.battle.data.MonsterData;
import com.htmake.htbot.discord.commands.battle.data.PlayerData;
import com.htmake.htbot.discord.commands.battle.data.status.extend.PlayerStatus;
import com.htmake.htbot.discord.skillAction.skill.impl.AbstractSkillStrategy;
import com.htmake.htbot.discord.skillAction.type.SkillType;
import kotlin.Pair;

import java.util.List;

public class S3304 extends AbstractSkillStrategy {

    private int manaCost;

    private void setManaCost(PlayerData playerData) {
        manaCost = (int) (playerData.getPlayerStatus().getMana() * 0.5);
    }

    @Override
    public boolean manaCheck(PlayerData playerData) {
        setManaCost(playerData);
        return super.manaCheck(playerData);
    }

    @Override
    protected int getManaCost() {
        return manaCost;
    }

    @Override
    protected void applySkill(PlayerData playerData, MonsterData monsterData, List<Pair<String, SkillType>> resultList) {
        PlayerStatus playerStatus = playerData.getPlayerStatus();

        int damageChange = (int) (manaCost * 0.1);
        playerStatus.setDamage(playerStatus.getDamage() + damageChange);
    }
}
