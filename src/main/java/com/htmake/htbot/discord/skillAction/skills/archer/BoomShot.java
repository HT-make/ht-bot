package com.htmake.htbot.discord.skillAction.skills.archer;

import com.htmake.htbot.discord.commands.battle.data.MonsterData;
import com.htmake.htbot.discord.commands.battle.data.PlayerData;
import com.htmake.htbot.discord.commands.battle.data.condition.Condition;
import com.htmake.htbot.discord.commands.battle.data.condition.extend.DamageOverTime;
import com.htmake.htbot.discord.commands.battle.data.status.extend.MonsterStatus;
import com.htmake.htbot.discord.commands.battle.data.status.extend.PlayerStatus;
import com.htmake.htbot.discord.skillAction.skills.SkillStrategy;
import com.htmake.htbot.discord.skillAction.type.SkillType;
import kotlin.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BoomShot implements SkillStrategy {

    @Override
    public List<Pair<String, SkillType>> execute(PlayerData playerData, MonsterData monsterData) {
        List<Pair<String, SkillType>> resultList = new ArrayList<>();

        PlayerStatus playerStatus = playerData.getPlayerStatus();
        MonsterStatus monsterStatus = monsterData.getMonsterStatus();

        int damage = critical((int) (playerStatus.getDamage() * 3.2), playerStatus.getCriticalDamage(), playerStatus.getCriticalChance());
        int damageReceived = Math.max(0, damage - monsterStatus.getDefence());
        monsterStatus.setHealth(Math.max(0, monsterStatus.getHealth() - damageReceived));

        Map<String, Condition> monsterCondition = monsterStatus.getConditionMap();
        DamageOverTime damageOverTime = new DamageOverTime("fire", "화상I", ":fire:", 3, (int) (playerStatus.getDamage() * 0.5));
        monsterCondition.put("fire", damageOverTime);

        resultList.add(new Pair<>(String.valueOf(damageReceived), SkillType.ATTACK));
        resultList.add(new Pair<>("화상I", SkillType.DEBUFF));

        return resultList;
    }
}
