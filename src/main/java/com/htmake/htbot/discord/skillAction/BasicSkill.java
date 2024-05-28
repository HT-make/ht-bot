package com.htmake.htbot.discord.skillAction;

import com.htmake.htbot.discord.commands.battle.data.MonsterStatus;
import com.htmake.htbot.discord.commands.battle.data.PlayerStatus;
import com.htmake.htbot.discord.skillAction.skills.SkillStrategy;
import kotlin.Pair;
import lombok.Getter;

import java.util.List;

@Getter
public class BasicSkill {

    private final SkillStrategy strategy;

    public BasicSkill(SkillStrategy strategy) {
        this.strategy = strategy;
    }

    public List<Pair<String, String>> execute(PlayerStatus player, MonsterStatus monster) {
        return strategy.execute(player, monster);
    }
}
