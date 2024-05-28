package com.htmake.htbot.discord.skillAction.skills.archer;

import com.htmake.htbot.discord.commands.battle.data.MonsterStatus;
import com.htmake.htbot.discord.commands.battle.data.PlayerStatus;
import com.htmake.htbot.discord.skillAction.skills.SkillStrategy;
import kotlin.Pair;

import java.util.ArrayList;
import java.util.List;

public class Breathing implements SkillStrategy {

    @Override
    public List<Pair<String, String>> execute(PlayerStatus player, MonsterStatus monster) {
        List<Pair<String, String>> resultList = new ArrayList<>();

        int healing = (int) (player.getDamage() * 1.5);
        player.setHealth(player.getHealth() + healing);

        resultList.add(new Pair<>(String.valueOf(healing), "HEAL"));

        return resultList;
    }
}
