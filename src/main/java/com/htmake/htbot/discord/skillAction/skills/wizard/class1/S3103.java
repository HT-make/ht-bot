package com.htmake.htbot.discord.skillAction.skills.wizard.class1;

import com.htmake.htbot.discord.commands.battle.data.MonsterData;
import com.htmake.htbot.discord.commands.battle.data.PlayerData;
import com.htmake.htbot.discord.commands.battle.data.status.extend.MonsterStatus;
import com.htmake.htbot.discord.skillAction.condition.Condition;
import com.htmake.htbot.discord.skillAction.condition.extend.etc.Faint;
import com.htmake.htbot.discord.skillAction.skill.action.SkillAction;
import com.htmake.htbot.discord.skillAction.skill.impl.AbstractSkillStrategy;
import com.htmake.htbot.discord.skillAction.type.SkillType;
import com.htmake.htbot.discord.util.RandomUtil;
import com.htmake.htbot.domain.player.enums.Job;
import kotlin.Pair;

import java.util.List;
import java.util.Map;

public class S3103 extends AbstractSkillStrategy {

    @Override
    public void setPassiveOn(Job job) {
        passiveOn = job.equals(Job.SKILLED_WIZARD) || job.getLowerJob().contains(Job.SKILLED_WIZARD);
    }

    @Override
    protected int getManaCost() {
        return passiveOn ? 50 : 25;
    }

    @Override
    protected void applySkill(PlayerData playerData, MonsterData monsterData, List<Pair<String, SkillType>> resultList) {
        MonsterStatus monsterStatus = monsterData.getMonsterStatus();
        Map<String, Condition> monsterCondition = monsterStatus.getConditionMap();

        SkillAction skillAction = new SkillAction();
        double skillValue = passiveOn ? 3.0 : 2.7;
        skillAction.attack(skillValue, playerData, monsterData, resultList);

        if (passiveOn && RandomUtil.randomPercentage(50)) {
            Faint faint = new Faint(1);
            skillAction.debuff(monsterCondition, faint, resultList);
        }
    }
}
