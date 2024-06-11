package com.htmake.htbot.discord.skillAction.skills.archer.class4;

import com.htmake.htbot.discord.commands.battle.data.MonsterData;
import com.htmake.htbot.discord.commands.battle.data.PlayerData;
import com.htmake.htbot.discord.skillAction.condition.Condition;
import com.htmake.htbot.discord.commands.battle.data.status.extend.MonsterStatus;
import com.htmake.htbot.discord.commands.battle.data.status.extend.PlayerStatus;
import com.htmake.htbot.discord.skillAction.condition.extend.Faint;
import com.htmake.htbot.discord.skillAction.skill.impl.AbstractSkillStrategy;
import com.htmake.htbot.discord.skillAction.type.SkillType;
import com.htmake.htbot.discord.util.RandomUtil;
import kotlin.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class S2402 extends AbstractSkillStrategy {

    private boolean isBuffed;

    private void setBuffedState(PlayerData playerData) {
        PlayerStatus playerStatus = playerData.getPlayerStatus();
        Map<String, Condition> playerCondition = playerStatus.getConditionMap();
        isBuffed = playerCondition.containsKey("divine_beast");
    }

    @Override
    protected int getManaCost() {
        return isBuffed ? 90 : 120;
    }

    @Override
    public List<Pair<String, SkillType>> execute(PlayerData playerData, MonsterData monsterData) {
        List<Pair<String, SkillType>> resultList = new ArrayList<>();

        setBuffedState(playerData);

        if (manaCheck(playerData.getPlayerStatus(), getManaCost(), resultList)) {
            return resultList;
        }

        applySkill(playerData, monsterData, resultList);

        return resultList;
    }

    @Override
    protected void applySkill(PlayerData playerData, MonsterData monsterData, List<Pair<String, SkillType>> resultList) {
        PlayerStatus playerStatus = playerData.getPlayerStatus();
        MonsterStatus monsterStatus = monsterData.getMonsterStatus();
        Map<String, Condition> monsterCondition = monsterStatus.getConditionMap();

        int damage = critical((int) (playerStatus.getDamage() * 5.0), playerStatus.getCriticalDamage(), playerStatus.getCriticalChance());
        int damageReceived = Math.max(0, damage - monsterStatus.getDefence());
        monsterStatus.setHealth(Math.max(0, monsterStatus.getHealth() - damageReceived));

        resultList.add(new Pair<>(String.valueOf(damageReceived), SkillType.ATTACK));

        int value = isBuffed ? 50 : 75;

        if (RandomUtil.randomPercentage(value)) {
            Faint faint = new Faint("faint", "기절I", ":dizzy:", 2, 50);
            monsterCondition.put("faint", faint);
            resultList.add(new Pair<>("기절I", SkillType.DEBUFF));
        }
    }
}
