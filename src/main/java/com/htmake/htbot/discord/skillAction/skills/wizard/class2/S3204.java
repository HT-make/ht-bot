package com.htmake.htbot.discord.skillAction.skills.wizard.class2;

import com.htmake.htbot.discord.commands.battle.data.MonsterData;
import com.htmake.htbot.discord.commands.battle.data.PlayerData;
import com.htmake.htbot.discord.skillAction.skill.action.SkillAction;
import com.htmake.htbot.discord.skillAction.skill.impl.AbstractSkillStrategy;
import com.htmake.htbot.discord.skillAction.type.SkillType;
import kotlin.Pair;

import java.util.List;

public class S3204 extends AbstractSkillStrategy {

    @Override
    protected int getManaCost() {
        return 80;
    }

    @Override
    protected void applySkill(PlayerData playerData, MonsterData monsterData, List<Pair<String, SkillType>> resultList) {
        SkillAction skillAction = new SkillAction();
        skillAction.attack(4.0, playerData, monsterData, resultList);
    }
}
