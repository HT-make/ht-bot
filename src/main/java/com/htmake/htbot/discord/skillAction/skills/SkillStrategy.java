package com.htmake.htbot.discord.skillAction.skills;

import com.htmake.htbot.discord.commands.battle.data.MonsterData;
import com.htmake.htbot.discord.commands.battle.data.PlayerData;
import com.htmake.htbot.discord.commands.battle.data.condition.Condition;
import com.htmake.htbot.discord.commands.battle.data.condition.extend.Buff;
import com.htmake.htbot.discord.commands.battle.data.status.BasicStatus;
import com.htmake.htbot.discord.skillAction.type.SkillType;
import kotlin.Pair;
import org.apache.commons.math3.random.MersenneTwister;
import org.apache.commons.math3.random.RandomGenerator;

import java.util.List;
import java.util.Map;

public interface SkillStrategy {

    List<Pair<String, SkillType>> execute(PlayerData playerData, MonsterData monsterData);

    default int critical(int damage, int criticalDamage, int criticalChance) {
        RandomGenerator random = new MersenneTwister();
        int chance = random.nextInt(100);

        if (chance < criticalChance) {
            double criticalDamageMultiple = (double) criticalDamage / 100;
            damage = (int) (damage * criticalDamageMultiple);
        }

        return damage;
    }

    default void buffCheck(String id, BasicStatus status, BasicStatus originalStatus, Map<String, Condition> conditionMap) {
        if (conditionMap.containsKey(id)) {
            Buff existsBuff = (Buff) conditionMap.get(id);
            existsBuff.unapply(status, originalStatus);
        }
    }
}
