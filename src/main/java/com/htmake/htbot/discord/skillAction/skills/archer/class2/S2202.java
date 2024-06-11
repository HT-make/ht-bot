package com.htmake.htbot.discord.skillAction.skills.archer.class2;

import com.htmake.htbot.discord.commands.battle.data.MonsterData;
import com.htmake.htbot.discord.commands.battle.data.PlayerData;
import com.htmake.htbot.discord.skillAction.condition.Condition;
import com.htmake.htbot.discord.skillAction.condition.extend.DamageOverTime;
import com.htmake.htbot.discord.commands.battle.data.status.extend.MonsterStatus;
import com.htmake.htbot.discord.commands.battle.data.status.extend.PlayerStatus;
import com.htmake.htbot.discord.skillAction.skill.impl.AbstractSkillStrategy;
import com.htmake.htbot.discord.skillAction.type.SkillType;
import kotlin.Pair;

import java.util.List;
import java.util.Map;

public class S2202 extends AbstractSkillStrategy {

    @Override
    protected int getManaCost() {
        return 60;
    }

    @Override
    protected void applySkill(PlayerData playerData, MonsterData monsterData, List<Pair<String, SkillType>> resultList) {
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
    }
}
