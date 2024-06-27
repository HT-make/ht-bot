package com.htmake.htbot.discord.skillAction.skills.wizard.class2;

import com.htmake.htbot.discord.commands.battle.data.MonsterData;
import com.htmake.htbot.discord.commands.battle.data.PlayerData;
import com.htmake.htbot.discord.commands.battle.data.status.extend.MonsterOriginalStatus;
import com.htmake.htbot.discord.commands.battle.data.status.extend.MonsterStatus;
import com.htmake.htbot.discord.skillAction.condition.Condition;
import com.htmake.htbot.discord.skillAction.condition.extend.damageOverTime.extend.Poison;
import com.htmake.htbot.discord.skillAction.skill.action.SkillAction;
import com.htmake.htbot.discord.skillAction.skill.impl.AbstractSkillStrategy;
import com.htmake.htbot.discord.skillAction.type.SkillType;
import kotlin.Pair;

import java.util.List;
import java.util.Map;

public class S3202 extends AbstractSkillStrategy {

    @Override
    protected int getManaCost() {
        return 100;
    }

    @Override
    protected void applySkill(PlayerData playerData, MonsterData monsterData, List<Pair<String, SkillType>> resultList) {
        MonsterStatus monsterStatus = monsterData.getMonsterStatus();
        MonsterOriginalStatus monsterOriginalStatus = monsterData.getMonsterOriginalStatus();
        Map<String, Condition> monsterCondition = monsterStatus.getConditionMap();

        SkillAction skillAction = new SkillAction();
        Poison poison = new Poison(1);
        skillAction.debuff(monsterStatus, monsterOriginalStatus, monsterCondition, poison, resultList);
    }
}
