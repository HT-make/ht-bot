package com.htmake.htbot.discord.skillAction;

import com.htmake.htbot.discord.commands.battle.data.MonsterData;
import com.htmake.htbot.discord.commands.battle.data.PlayerData;
import com.htmake.htbot.discord.skillAction.skill.SkillStrategy;
import com.htmake.htbot.discord.skillAction.type.SkillType;
import kotlin.Pair;
import lombok.Getter;

import java.util.List;

@Getter
public class BasicSkill {

    private final SkillStrategy strategy;

    public BasicSkill(SkillStrategy strategy) {
        this.strategy = strategy;
    }

    public List<Pair<String, SkillType>> execute(PlayerData playerData, MonsterData monsterData) {
        return strategy.execute(playerData, monsterData);
    }

    public boolean manaCheck(PlayerData playerData) {
        return strategy.manaCheck(playerData);
    }
}
