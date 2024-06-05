package com.htmake.htbot.discord.skillAction.skills.archer.class4;

import com.htmake.htbot.discord.commands.battle.data.MonsterData;
import com.htmake.htbot.discord.commands.battle.data.PlayerData;
import com.htmake.htbot.discord.skillAction.condition.Condition;
import com.htmake.htbot.discord.commands.battle.data.status.extend.MonsterStatus;
import com.htmake.htbot.discord.commands.battle.data.status.extend.PlayerStatus;
import com.htmake.htbot.discord.skillAction.skills.SkillStrategy;
import com.htmake.htbot.discord.skillAction.type.SkillType;
import kotlin.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class S2401 implements SkillStrategy {

    @Override
    public List<Pair<String, SkillType>> execute(PlayerData playerData, MonsterData monsterData) {
        List<Pair<String, SkillType>> resultList = new ArrayList<>();

        PlayerStatus playerStatus = playerData.getPlayerStatus();
        Map<String, Condition> playerCondition = playerData.getPlayerStatus().getConditionMap();
        MonsterStatus monsterStatus = monsterData.getMonsterStatus();

        double skillValue = playerCondition.containsKey("divine_beast") ? 4.5 : 3.0;

        int damage = critical((int) (playerStatus.getDamage() * skillValue), playerStatus.getCriticalDamage(), playerStatus.getCriticalChance());
        int damageReceived = Math.max(0, damage - monsterStatus.getDefence());
        monsterStatus.setHealth(Math.max(0, monsterStatus.getHealth() - damageReceived));

        resultList.add(new Pair<>(String.valueOf(damageReceived), SkillType.ATTACK));

        return resultList;
    }
}
