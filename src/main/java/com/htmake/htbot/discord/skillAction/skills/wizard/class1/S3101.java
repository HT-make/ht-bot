package com.htmake.htbot.discord.skillAction.skills.wizard.class1;

import com.htmake.htbot.discord.commands.battle.data.MonsterData;
import com.htmake.htbot.discord.commands.battle.data.PlayerData;
import com.htmake.htbot.discord.commands.battle.data.status.extend.MonsterStatus;
import com.htmake.htbot.discord.commands.battle.data.status.extend.PlayerStatus;
import com.htmake.htbot.discord.skillAction.condition.Condition;
import com.htmake.htbot.discord.skillAction.condition.extend.damageOverTime.extend.Fire;
import com.htmake.htbot.discord.skillAction.skill.action.SkillAction;
import com.htmake.htbot.discord.skillAction.skill.impl.AbstractSkillStrategy;
import com.htmake.htbot.discord.skillAction.type.SkillType;
import com.htmake.htbot.discord.util.RandomUtil;
import com.htmake.htbot.domain.player.enums.Job;
import kotlin.Pair;

import java.util.List;
import java.util.Map;

public class S3101 extends AbstractSkillStrategy {

    @Override
    public void setPassiveOn(Job job) {
        passiveOn = job.equals(Job.SKILLED_WIZARD) || job.getLowerJob().contains(Job.SKILLED_WIZARD);
    }

    @Override
    protected int getManaCost() {
        return passiveOn ? 40 : 20;
    }

    @Override
    protected void applySkill(PlayerData playerData, MonsterData monsterData, List<Pair<String, SkillType>> resultList) {
        PlayerStatus playerStatus = playerData.getPlayerStatus();
        MonsterStatus monsterStatus = monsterData.getMonsterStatus();
        Map<String, Condition> monsterCondition = monsterStatus.getConditionMap();

        SkillAction skillAction = new SkillAction();
        double skillValue = passiveOn ? 2.8 : 2.3;
        skillAction.attack(skillValue, playerData, monsterData, resultList);

        if (RandomUtil.randomPercentage(100)) {
            Fire fire = new Fire(1);
            skillAction.debuff(playerStatus, monsterCondition, fire, resultList);
        }
    }
}
