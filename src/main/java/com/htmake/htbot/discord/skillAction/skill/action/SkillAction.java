package com.htmake.htbot.discord.skillAction.skill.action;

import com.htmake.htbot.discord.commands.battle.action.ConditionAction;
import com.htmake.htbot.discord.commands.battle.data.MonsterData;
import com.htmake.htbot.discord.commands.battle.data.PlayerData;
import com.htmake.htbot.discord.commands.battle.data.status.BasicStatus;
import com.htmake.htbot.discord.commands.battle.data.status.extend.MonsterStatus;
import com.htmake.htbot.discord.commands.battle.data.status.extend.PlayerStatus;
import com.htmake.htbot.discord.skillAction.condition.Condition;
import com.htmake.htbot.discord.skillAction.condition.extend.buff.Buff;
import com.htmake.htbot.discord.skillAction.condition.extend.damageOverTime.DamageOverTime;
import com.htmake.htbot.discord.skillAction.condition.extend.damageOverTime.extend.Poison;
import com.htmake.htbot.discord.skillAction.type.SkillType;
import com.htmake.htbot.discord.util.RandomUtil;
import com.htmake.htbot.domain.player.enums.Job;
import kotlin.Pair;

import java.util.List;
import java.util.Map;

public class SkillAction {

    private int damageReceived;
    private boolean isCritical;

    private int additionalDamage;
    private int additionalCriticalDamage;
    private int additionalCriticalChance;
    private double ignoreDefense;
    private Job job;

    private final ConditionAction conditionAction;

    public SkillAction() {
        this.damageReceived = 0;
        this.isCritical = false;

        this.conditionAction = new ConditionAction();
    }

    public int getDamageReceived() {
        return this.damageReceived;
    }

    public boolean isCritical() {
        return this.isCritical;
    }

    public SkillAction additionalDamage(int additionalDamage) {
        this.additionalDamage = additionalDamage;
        return this;
    }

    public SkillAction additionalCriticalDamage(int additionalCriticalDamage) {
        this.additionalCriticalDamage = additionalCriticalDamage;
        return this;
    }

    public SkillAction additionalCriticalChance(int additionalCriticalChance) {
        this.additionalCriticalChance = additionalCriticalChance;
        return this;
    }

    public SkillAction ignoreDefense(double ignoreDefense) {
        this.ignoreDefense = ignoreDefense;
        return this;
    }

    public SkillAction passive(Job job) {
        this.job = job;
        return this;
    }

    public void attack(double skillValue, PlayerData playerData, MonsterData monsterData, List<Pair<String, SkillType>> resultList) {
        PlayerStatus playerStatus = playerData.getPlayerStatus();
        MonsterStatus monsterStatus = monsterData.getMonsterStatus();

        damageReceived = (calculateDamage(skillValue, playerStatus) + additionalDamage) - getDefence(monsterStatus.getDefence());

        conditionAction.effectProcessing(playerData, monsterData, damageReceived, true);

        monsterStatus.setHealth(Math.max(0, monsterStatus.getHealth() - damageReceived));
        resultList.add(new Pair<>(String.valueOf(damageReceived), SkillType.ATTACK));
    }

    private int calculateDamage(double skillValue, BasicStatus playerStatus) {
        int skilledDamage = (int) (playerStatus.getDamage() * skillValue);

        if (RandomUtil.randomPercentage(playerStatus.getCriticalChance() + additionalCriticalChance)) {
            isCritical = true;

            if (job != null) {
                return jobSpecificCriticalDamage(skilledDamage, playerStatus);
            }

            return critical(skilledDamage, playerStatus.getCriticalDamage());
        }
        return skilledDamage;
    }

    private int jobSpecificCriticalDamage(int skilledDamage, BasicStatus playerStatus) {
        int criticalDamage = playerStatus.getCriticalDamage();

        if (job.equals(Job.SNIPER)) {
            int totalCriticalChance = additionalCriticalDamage + playerStatus.getCriticalChance();
            return critical(skilledDamage, criticalDamage + totalCriticalChance / 2);
        }

        if (job.equals(Job.SPIRIT_ARCHER)) {
            int damageWithCritical = critical(skilledDamage, criticalDamage);
            if (RandomUtil.randomPercentage(30)) {
                return critical(damageWithCritical, criticalDamage);
            }
            return damageWithCritical;
        }

        return critical(skilledDamage, criticalDamage);
    }

    private int critical(int damage, int criticalDamage) {
        double criticalDamageMultiple = (double) (criticalDamage + additionalCriticalDamage) / 100;
        return  (int) (damage * criticalDamageMultiple);
    }

    private int getDefence(int defence) {
        int deductedDefense = (int) (defence * ignoreDefense);
        return Math.max(0, defence - deductedDefense);
    }

    public void heal(int healing, BasicStatus status, BasicStatus originalStatus, List<Pair<String, SkillType>> resultList) {
        status.setHealth(Math.min(originalStatus.getHealth(), status.getHealth() + healing));
        resultList.add(new Pair<>(String.valueOf(healing), SkillType.HEAL));
    }

    public void buff(
            BasicStatus status,
            BasicStatus originalStatus,
            Map<String, Condition> conditionMap,
            Condition condition,
            List<Pair<String, SkillType>> resultList
    ) {
        applyCondition(status, originalStatus, conditionMap, condition, resultList, SkillType.BUFF);
    }

    public void debuff(
            BasicStatus status,
            BasicStatus originalStatus,
            Map<String, Condition> conditionMap,
            Condition condition,
            List<Pair<String, SkillType>> resultList
    ) {
        applyCondition(status, originalStatus, conditionMap, condition, resultList, SkillType.DEBUFF);
    }

    public void debuff(BasicStatus status, Map<String, Condition> conditionMap, Condition condition, List<Pair<String, SkillType>> resultList) {
        applyCondition(status, null, conditionMap, condition, resultList, SkillType.DEBUFF);
    }

    public void debuff(Map<String, Condition> conditionMap, Condition condition, List<Pair<String, SkillType>> resultList) {
        applyCondition(null, null, conditionMap, condition, resultList, SkillType.DEBUFF);
    }

    private void applyCondition(
            BasicStatus status,
            BasicStatus originalStatus,
            Map<String, Condition> conditionMap,
            Condition condition,
            List<Pair<String, SkillType>> resultList,
            SkillType skillType
    ) {
        if (condition instanceof Buff buff) {
            if (conditionMap.containsKey(buff.getId())) {
                buff.unapply(status, originalStatus);
            }
            buff.apply(status, originalStatus);
        } else if (condition instanceof DamageOverTime damageOverTime) {
            if (damageOverTime instanceof Poison poison) {
                poison.setEffectDamage(originalStatus.getHealth());
            } else {
                damageOverTime.setEffectDamage(status.getDamage());
            }
        }

        conditionMap.put(condition.getId(), condition);
        resultList.add(new Pair<>(condition.getName(), skillType));
    }
}
