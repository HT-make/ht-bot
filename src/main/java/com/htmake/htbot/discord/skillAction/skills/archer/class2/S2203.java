package com.htmake.htbot.discord.skillAction.skills.archer.class2;

import com.htmake.htbot.discord.commands.battle.data.MonsterData;
import com.htmake.htbot.discord.commands.battle.data.PlayerData;
import com.htmake.htbot.discord.skillAction.condition.Condition;
import com.htmake.htbot.discord.skillAction.condition.extend.Buff;
import com.htmake.htbot.discord.commands.battle.data.status.extend.MonsterOriginalStatus;
import com.htmake.htbot.discord.commands.battle.data.status.extend.MonsterStatus;
import com.htmake.htbot.discord.commands.battle.data.status.extend.PlayerStatus;
import com.htmake.htbot.discord.skillAction.skill.impl.AbstractSkillStrategy;
import com.htmake.htbot.discord.skillAction.type.BuffStatus;
import com.htmake.htbot.discord.skillAction.type.BuffType;
import com.htmake.htbot.discord.skillAction.type.SkillType;
import kotlin.Pair;

import java.util.List;
import java.util.Map;

public class S2203 extends AbstractSkillStrategy {

    @Override
    protected int getManaCost() {
        return 40;
    }

    @Override
    protected void applySkill(PlayerData playerData, MonsterData monsterData, List<Pair<String, SkillType>> resultList) {
        PlayerStatus playerStatus = playerData.getPlayerStatus();
        MonsterStatus monsterStatus = monsterData.getMonsterStatus();
        MonsterOriginalStatus monsterOriginalStatus = monsterData.getMonsterOriginalStatus();

        int damage = critical((int) (playerStatus.getDamage() * 2.0), playerStatus.getCriticalDamage(), playerStatus.getCriticalChance());
        int damageReceived = Math.max(0, damage - monsterStatus.getDefence());
        monsterStatus.setHealth(Math.max(0, monsterStatus.getHealth() - damageReceived));

        Map<String, Condition> monsterCondition = monsterStatus.getConditionMap();
        Buff buff = new Buff(
                "blind",
                "실명I",
                ":eye:",
                3,
                0.10,
                BuffType.DOWN,
                BuffStatus.CRITICAL_CHANCE
        );

        buffCheck("blind", monsterStatus, monsterOriginalStatus, monsterCondition);

        monsterCondition.put("blind", buff);

        buff.apply(monsterStatus, monsterOriginalStatus);

        resultList.add(new Pair<>(String.valueOf(damageReceived), SkillType.ATTACK));
        resultList.add(new Pair<>("실명I", SkillType.DEBUFF));
    }
}
