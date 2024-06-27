package com.htmake.htbot.discord.skillAction.skill;

import com.htmake.htbot.discord.commands.battle.data.MonsterData;
import com.htmake.htbot.discord.commands.battle.data.PlayerData;
import com.htmake.htbot.discord.skillAction.condition.Condition;
import com.htmake.htbot.discord.commands.battle.data.status.BasicStatus;
import com.htmake.htbot.discord.skillAction.type.SkillType;
import com.htmake.htbot.domain.player.enums.Job;
import kotlin.Pair;

import java.util.List;
import java.util.Map;

public interface SkillStrategy {

    List<Pair<String, SkillType>> execute(PlayerData playerData, MonsterData monsterData);

    void setPassiveOn(Job job);

    boolean manaCheck(PlayerData playerData);

    int critical(int damage, int criticalDamage, int criticalChance);

    void buffCheck(String id, BasicStatus status, BasicStatus originalStatus, Map<String, Condition> conditionMap);
}
