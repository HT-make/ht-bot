package com.htmake.htbot.discord.skillAction.skills.wizard.class1;

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
import com.htmake.htbot.discord.util.RandomUtil;
import com.htmake.htbot.domain.player.enums.Job;
import kotlin.Pair;

import java.util.List;
import java.util.Map;

public class S3102 extends AbstractSkillStrategy {

    @Override
    public void setPassiveOn(Job job) {
        passiveOn = job.equals(Job.SKILLED_WIZARD) || job.getLowerJob().contains(Job.SKILLED_WIZARD);
    }

    @Override
    protected int getManaCost() {
        return passiveOn ? 45 : 20;
    }

    @Override
    protected void applySkill(PlayerData playerData, MonsterData monsterData, List<Pair<String, SkillType>> resultList) {
        PlayerStatus playerStatus = playerData.getPlayerStatus();
        MonsterStatus monsterStatus = monsterData.getMonsterStatus();
        MonsterOriginalStatus monsterOriginalStatus = monsterData.getMonsterOriginalStatus();
        Map<String, Condition> monsterCondition = monsterStatus.getConditionMap();

        SkillAction skillAction = new SkillAction();
        double skillValue = passiveOn ? 2.7 : 2.0;
        skillAction.attack(skillValue, playerStatus, monsterStatus, resultList);

        if (RandomUtil.randomPercentage(40)) {
            Frostbite frostbite = new Frostbite(1);
            skillAction.debuff(monsterStatus, monsterOriginalStatus, monsterCondition, frostbite, resultList);
        }
    }
}
