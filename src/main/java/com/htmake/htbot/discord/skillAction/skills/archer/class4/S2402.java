package com.htmake.htbot.discord.skillAction.skills.archer.class4;

import com.htmake.htbot.discord.commands.battle.data.MonsterData;
import com.htmake.htbot.discord.commands.battle.data.PlayerData;
import com.htmake.htbot.discord.skillAction.condition.Condition;
import com.htmake.htbot.discord.commands.battle.data.status.extend.MonsterStatus;
import com.htmake.htbot.discord.commands.battle.data.status.extend.PlayerStatus;
import com.htmake.htbot.discord.skillAction.condition.extend.Faint;
import com.htmake.htbot.discord.skillAction.skills.SkillStrategy;
import com.htmake.htbot.discord.skillAction.type.SkillType;
import com.htmake.htbot.discord.util.RandomUtil;
import kotlin.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class S2402 implements SkillStrategy {

    @Override
    public List<Pair<String, SkillType>> execute(PlayerData playerData, MonsterData monsterData) {
        List<Pair<String, SkillType>> resultList = new ArrayList<>();

        PlayerStatus playerStatus = playerData.getPlayerStatus();
        Map<String, Condition> playerCondition = playerData.getPlayerStatus().getConditionMap();
        MonsterStatus monsterStatus = monsterData.getMonsterStatus();
        Map<String, Condition> monsterCondition = monsterData.getMonsterStatus().getConditionMap();

        int damage = critical((int) (playerStatus.getDamage() * 5.0), playerStatus.getCriticalDamage(), playerStatus.getCriticalChance());
        int damageReceived = Math.max(0, damage - monsterStatus.getDefence());
        monsterStatus.setHealth(Math.max(0, monsterStatus.getHealth() - damageReceived));

        resultList.add(new Pair<>(String.valueOf(damageReceived), SkillType.ATTACK));

        int value = playerCondition.containsKey("divine_beast") ? 50 : 75;

        if (RandomUtil.randomPercentage(value)) {
            Faint faint = new Faint("faint", "기절I", ":dizzy:", 2, 50);
            monsterCondition.put("faint", faint);
            resultList.add(new Pair<>("기절I", SkillType.DEBUFF));
        }

        return resultList;
    }
}
