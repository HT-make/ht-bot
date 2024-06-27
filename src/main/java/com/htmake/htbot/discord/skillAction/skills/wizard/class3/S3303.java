package com.htmake.htbot.discord.skillAction.skills.wizard.class3;

import com.htmake.htbot.discord.commands.battle.data.MonsterData;
import com.htmake.htbot.discord.commands.battle.data.PlayerData;
import com.htmake.htbot.discord.commands.battle.data.status.extend.MonsterOriginalStatus;
import com.htmake.htbot.discord.commands.battle.data.status.extend.MonsterStatus;
import com.htmake.htbot.discord.commands.battle.data.status.extend.PlayerStatus;
import com.htmake.htbot.discord.skillAction.condition.Condition;
import com.htmake.htbot.discord.skillAction.condition.extend.buff.extend.Frostbite;
import com.htmake.htbot.discord.skillAction.skill.action.SkillAction;
import com.htmake.htbot.discord.skillAction.skill.impl.AbstractSkillStrategy;
import com.htmake.htbot.discord.skillAction.type.SkillType;
import kotlin.Pair;

import java.util.List;
import java.util.Map;

public class S3303 extends AbstractSkillStrategy {

    @Override
    protected int getManaCost() {
        return 0;
    }

    @Override
    protected void applySkill(PlayerData playerData, MonsterData monsterData, List<Pair<String, SkillType>> resultList) {
        PlayerStatus playerStatus = playerData.getPlayerStatus();
        MonsterStatus monsterStatus = monsterData.getMonsterStatus();
        MonsterOriginalStatus monsterOriginalStatus = monsterData.getMonsterOriginalStatus();
        Map<String, Condition> monsterCondition = monsterStatus.getConditionMap();

        SkillAction skillAction = new SkillAction();
        skillAction.attack(4.5, playerStatus, monsterStatus, resultList);
        Frostbite frostbite = new Frostbite(2);
        skillAction.debuff(monsterStatus, monsterOriginalStatus, monsterCondition, frostbite, resultList);
    }
}
