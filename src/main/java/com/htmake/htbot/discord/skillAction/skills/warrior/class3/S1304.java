package com.htmake.htbot.discord.skillAction.skills.warrior.class3;

import com.htmake.htbot.discord.commands.battle.data.MonsterData;
import com.htmake.htbot.discord.commands.battle.data.PlayerData;
import com.htmake.htbot.discord.commands.battle.data.status.extend.MonsterStatus;
import com.htmake.htbot.discord.commands.battle.data.status.extend.PlayerOriginalStatus;
import com.htmake.htbot.discord.commands.battle.data.status.extend.PlayerStatus;
import com.htmake.htbot.discord.skillAction.condition.Condition;
import com.htmake.htbot.discord.skillAction.condition.extend.SwordGod;
import com.htmake.htbot.discord.skillAction.skill.impl.AbstractSkillStrategy;
import com.htmake.htbot.discord.skillAction.type.SkillType;
import kotlin.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class S1304 extends AbstractSkillStrategy {

    private boolean isBuffed;

    private void setBuffedState(PlayerData playerData) {
        PlayerStatus playerStatus = playerData.getPlayerStatus();
        Map<String, Condition> playerCondition = playerStatus.getConditionMap();
        isBuffed = playerCondition.containsKey("sword_god");
    }

    @Override
    protected int getManaCost() {
        return isBuffed ? 0 : 100;
    }

    @Override
    public List<Pair<String, SkillType>> execute(PlayerData playerData, MonsterData monsterData) {
        List<Pair<String, SkillType>> resultList = new ArrayList<>();
        setBuffedState(playerData);
        applySkill(playerData, monsterData, resultList);
        return resultList;
    }

    @Override
    public boolean manaCheck(PlayerData playerData) {
        PlayerStatus playerStatus = playerData.getPlayerStatus();
        setBuffedState(playerData);
        int mana = playerStatus.getMana();

        if (mana < getManaCost()) {
            return false;
        }

        playerStatus.setMana(mana - getManaCost());

        return true;
    }

    @Override
    protected void applySkill(PlayerData playerData, MonsterData monsterData, List<Pair<String, SkillType>> resultList) {
        PlayerStatus playerStatus = playerData.getPlayerStatus();
        PlayerOriginalStatus playerOriginalStatus = playerData.getPlayerOriginalStatus();
        Map<String, Condition> playerCondition = playerStatus.getConditionMap();
        MonsterStatus monsterStatus = monsterData.getMonsterStatus();

        if (isBuffed) {
            SwordGod swordGod = (SwordGod) playerCondition.get("sword_god");
            double additionValue = (5 - swordGod.getTurn()) * 0.5;
            double skillValue = 5.0 + additionValue;

            int damage = critical((int) (playerStatus.getDamage() * skillValue), playerStatus.getCriticalDamage(), playerStatus.getCriticalChance());
            int damageReceived = Math.max(0, damage - monsterStatus.getDefence());
            monsterStatus.setHealth(Math.max(0, monsterStatus.getHealth() - damageReceived));

            swordGod.unapply(playerStatus, playerOriginalStatus);
            playerCondition.remove(swordGod.getId());

            resultList.add(new Pair<>(String.valueOf(damageReceived), SkillType.ATTACK));
        } else {
            int currentHealth = playerStatus.getHealth();
            int requiredHealth = (int) (playerOriginalStatus.getHealth() * 0.3);
            int minimumHealth = (int) (playerOriginalStatus.getHealth() * 0.01);

            playerStatus.setHealth(Math.max(minimumHealth, currentHealth - requiredHealth));

            SwordGod swordGod = new SwordGod();
            playerCondition.put(swordGod.getId(), swordGod);
            swordGod.apply(playerStatus, playerOriginalStatus);

            resultList.add(new Pair<>(swordGod.getName(), SkillType.BUFF));
        }
    }
}
