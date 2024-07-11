package com.htmake.htbot.discord.skillAction.skills.wizard.class2;

import com.htmake.htbot.discord.commands.battle.data.MonsterData;
import com.htmake.htbot.discord.commands.battle.data.PlayerData;
import com.htmake.htbot.discord.skillAction.skill.action.SkillAction;
import com.htmake.htbot.discord.skillAction.skill.impl.AbstractSkillStrategy;
import com.htmake.htbot.discord.skillAction.type.SkillType;
import com.htmake.htbot.discord.util.RandomUtil;
import kotlin.Pair;

import java.util.List;

public class S3201 extends AbstractSkillStrategy {

    @Override
    protected int getManaCost() {
        return 50;
    }

    @Override
    protected void applySkill(PlayerData playerData, MonsterData monsterData, List<Pair<String, SkillType>> resultList) {
        SkillAction skillAction = new SkillAction();
        int chance = 45;
        do {
            skillAction.attack(3.3, playerData, monsterData, resultList);
            chance -= 5;
        } while (RandomUtil.randomPercentage(chance));
    }
}
