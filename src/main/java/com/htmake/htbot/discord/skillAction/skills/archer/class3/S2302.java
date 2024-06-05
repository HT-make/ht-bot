package com.htmake.htbot.discord.skillAction.skills.archer.class3;

import com.htmake.htbot.discord.commands.battle.data.MonsterData;
import com.htmake.htbot.discord.commands.battle.data.PlayerData;
import com.htmake.htbot.discord.skillAction.condition.Condition;
import com.htmake.htbot.discord.skillAction.condition.extend.DamageOverTime;
import com.htmake.htbot.discord.commands.battle.data.status.extend.MonsterStatus;
import com.htmake.htbot.discord.commands.battle.data.status.extend.PlayerStatus;
import com.htmake.htbot.discord.skillAction.skills.SkillStrategy;
import com.htmake.htbot.discord.skillAction.type.SkillType;
import kotlin.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class S2302 implements SkillStrategy {

    @Override
    public List<Pair<String, SkillType>> execute(PlayerData playerData, MonsterData monsterData) {
        List<Pair<String, SkillType>> resultList = new ArrayList<>();

        PlayerStatus playerStatus = playerData.getPlayerStatus();
        MonsterStatus monsterStatus = monsterData.getMonsterStatus();

        int damage = critical((int) (playerStatus.getDamage() * 4.0), playerStatus.getCriticalDamage(), playerStatus.getCriticalChance());
        int damageReceived = Math.max(0, damage - monsterStatus.getDefence());
        monsterStatus.setHealth(Math.max(0, monsterStatus.getHealth() - damageReceived));

        Map<String, Condition> monsterCondition = monsterStatus.getConditionMap();
        DamageOverTime damageOverTime = new DamageOverTime("bleeding", "출혈II", ":blood:", 5, (int) (playerStatus.getDamage() * 0.8));
        monsterCondition.put("bleeding", damageOverTime);

        resultList.add(new Pair<>(String.valueOf(damageReceived), SkillType.ATTACK));
        resultList.add(new Pair<>("출혈II", SkillType.DEBUFF));

        return resultList;
    }
}
