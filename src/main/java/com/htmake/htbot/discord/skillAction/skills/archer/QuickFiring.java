package com.htmake.htbot.discord.skillAction.skills.archer;

import com.htmake.htbot.discord.commands.battle.data.MonsterStatus;
import com.htmake.htbot.discord.commands.battle.data.PlayerStatus;
import com.htmake.htbot.discord.skillAction.skills.SkillStrategy;
import kotlin.Pair;

import java.util.ArrayList;
import java.util.List;

public class QuickFiring implements SkillStrategy {

    @Override
    public List<Pair<String, String>> execute(PlayerStatus player, MonsterStatus monster) {
        List<Pair<String, String>> resultList = new ArrayList<>();

        int damage = critical((int) (player.getDamage() * 1.5), player.getCriticalDamage(), player.getCriticalChance());
        int damageReceived = Math.max(0, damage - monster.getDefence());
        monster.setHealth(Math.max(0, monster.getHealth() - damageReceived));

        resultList.add(new Pair<>(String.valueOf(damageReceived), "ATTACK"));

        return resultList;
    }
}
