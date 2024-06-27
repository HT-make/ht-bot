package com.htmake.htbot.discord.skillAction.skill.impl;

import com.htmake.htbot.discord.commands.battle.data.MonsterData;
import com.htmake.htbot.discord.commands.battle.data.PlayerData;
import com.htmake.htbot.discord.commands.battle.data.status.BasicStatus;
import com.htmake.htbot.discord.commands.battle.data.status.extend.PlayerStatus;
import com.htmake.htbot.discord.skillAction.condition.Condition;
import com.htmake.htbot.discord.skillAction.condition.extend.buff.Buff;
import com.htmake.htbot.discord.skillAction.skill.SkillStrategy;
import com.htmake.htbot.discord.skillAction.type.SkillType;
import com.htmake.htbot.discord.util.RandomUtil;
import com.htmake.htbot.domain.player.enums.Job;
import kotlin.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class AbstractSkillStrategy implements SkillStrategy {

    protected boolean passiveOn;

    protected abstract int getManaCost();

    protected abstract void applySkill(PlayerData playerData, MonsterData monsterData, List<Pair<String, SkillType>> resultList);

    @Override
    public void setPassiveOn(Job job) {
        passiveOn = false;
    }

    @Override
    public List<Pair<String, SkillType>> execute(PlayerData playerData, MonsterData monsterData) {
        setPassiveOn(playerData.getPlayerStatus().getJob());

        List<Pair<String, SkillType>> resultList = new ArrayList<>();
        applySkill(playerData, monsterData, resultList);
        return resultList;
    }

    @Override
    public boolean manaCheck(PlayerData playerData) {
        setPassiveOn(playerData.getPlayerStatus().getJob());

        PlayerStatus playerStatus = playerData.getPlayerStatus();
        int mana = playerStatus.getMana();

        if (mana < getManaCost()) {
            return false;
        }

        playerStatus.setMana(mana - getManaCost());

        return true;
    }

    @Override
    public int critical(int damage, int criticalDamage, int criticalChance) {
        if (RandomUtil.randomPercentage(criticalChance)) {
            double criticalDamageMultiple = (double) criticalDamage / 100;
            damage = (int) (damage * criticalDamageMultiple);
        }

        return damage;
    }

    public int critical(int damage, int criticalDamage, int criticalChance, Job job) {
        if (!job.equals(Job.SNIPER) && !job.equals(Job.SPIRIT_ARCHER)) {
            return critical(damage, criticalDamage, criticalChance);
        }

        int newCriticalDamage = (job.equals(Job.SNIPER) ? (criticalDamage + criticalChance / 2) : (criticalDamage));

        if (RandomUtil.randomPercentage(criticalChance)) {
            double criticalDamageMultiple = (double) newCriticalDamage / 100;
            damage = (int) (damage * criticalDamageMultiple);

            if (job.equals(Job.SPIRIT_ARCHER) && RandomUtil.randomPercentage(30)) {
                damage = (int) (damage * criticalDamageMultiple);
            }
        }

        return damage;
    }

    @Override
    public void buffCheck(String id, BasicStatus status, BasicStatus originalStatus, Map<String, Condition> conditionMap) {
        if (conditionMap.containsKey(id)) {
            Buff existsBuff = (Buff) conditionMap.get(id);
            existsBuff.unapply(status, originalStatus);
        }
    }
}
