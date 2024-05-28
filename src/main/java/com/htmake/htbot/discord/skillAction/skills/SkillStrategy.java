package com.htmake.htbot.discord.skillAction.skills;

import com.htmake.htbot.discord.commands.battle.data.MonsterStatus;
import com.htmake.htbot.discord.commands.battle.data.PlayerStatus;
import kotlin.Pair;
import org.apache.commons.math3.random.MersenneTwister;
import org.apache.commons.math3.random.RandomGenerator;

import java.util.List;

public interface SkillStrategy {

    List<Pair<String, String>> execute(PlayerStatus player, MonsterStatus monster);

    default int critical(int damage, int criticalDamage, int criticalChance) {
        RandomGenerator random = new MersenneTwister();
        int chance = random.nextInt(100);

        if (chance < criticalChance) {
            double criticalDamageMultiple = (double) criticalDamage / 100;
            damage = (int) (damage * criticalDamageMultiple);
        }

        return damage;
    }
}
