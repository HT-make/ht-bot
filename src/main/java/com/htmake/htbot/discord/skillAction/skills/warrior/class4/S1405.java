package com.htmake.htbot.discord.skillAction.skills.warrior.class4;

import com.htmake.htbot.discord.commands.battle.data.MonsterData;
import com.htmake.htbot.discord.commands.battle.data.PlayerData;
import com.htmake.htbot.discord.commands.battle.data.status.extend.MonsterStatus;
import com.htmake.htbot.discord.commands.battle.data.status.extend.PlayerOriginalStatus;
import com.htmake.htbot.discord.commands.battle.data.status.extend.PlayerStatus;
import com.htmake.htbot.discord.skillAction.condition.Condition;
import com.htmake.htbot.discord.skillAction.condition.extend.AngelsProtection;
import com.htmake.htbot.discord.skillAction.condition.extend.Light;
import com.htmake.htbot.discord.skillAction.condition.extend.buff.Buff;
import com.htmake.htbot.discord.skillAction.condition.extend.buff.extend.Power;
import com.htmake.htbot.discord.skillAction.condition.extend.buff.extend.Protect;
import com.htmake.htbot.discord.skillAction.skill.impl.AbstractSkillStrategy;
import com.htmake.htbot.discord.skillAction.type.SkillType;
import com.htmake.htbot.discord.util.RandomUtil;
import kotlin.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class S1405 extends AbstractSkillStrategy {

    private int type;

    private void setType(PlayerData playerData) {
        Light light = (Light) playerData.getPlayerStatus().getConditionMap().get("light");

        if (light == null) {
            type = 1;
        } else {
            switch (light.getTurn()) {
                case 1 -> type = 1;
                case 2, 3 -> type = 2;
                case 4 -> type = 3;
                case 5 -> type = 4;
            }
        }
    }

    @Override
    protected int getManaCost() {
        return switch (type) {
            case 1 -> 40;
            case 2 -> 60;
            case 3 -> 90;
            case 4 -> 150;
            default -> 0;
        };
    }

    @Override
    public List<Pair<String, SkillType>> execute(PlayerData playerData, MonsterData monsterData) {
        List<Pair<String, SkillType>> resultList = new ArrayList<>();
        setType(playerData);
        applySkill(playerData, monsterData, resultList);
        return resultList;
    }

    @Override
    public boolean manaCheck(PlayerData playerData) {
        PlayerStatus playerStatus = playerData.getPlayerStatus();
        setType(playerData);
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

        switch (type) {
            case 1 -> firstType(playerStatus, playerOriginalStatus, playerCondition, resultList);
            case 2 -> secondType(playerStatus, playerOriginalStatus, playerCondition, monsterStatus, resultList);
            case 3 -> thirdType(playerStatus, playerOriginalStatus, playerCondition, monsterStatus, resultList);
            case 4 -> fourthType(playerStatus, playerOriginalStatus, playerCondition, monsterStatus, resultList);
        }
    }

    private void firstType(
            PlayerStatus playerStatus,
            PlayerOriginalStatus playerOriginalStatus,
            Map<String, Condition> playerCondition,
            List<Pair<String, SkillType>> resultList
    ) {
        int version = (RandomUtil.randomPercentage(80) ? 1 : 2);
        Buff buff = (RandomUtil.randomPercentage(50) ? new Power(version) : new Protect(version));

        buffCheck(buff.getId(), playerStatus, playerOriginalStatus, playerCondition);
        playerCondition.put(buff.getId(), buff);
        buff.apply(playerStatus, playerOriginalStatus);

        resultList.add(new Pair<>(buff.getName(), SkillType.BUFF));
    }

    private void secondType(
            PlayerStatus playerStatus,
            PlayerOriginalStatus playerOriginalStatus,
            Map<String, Condition> playerCondition,
            MonsterStatus monsterStatus,
            List<Pair<String, SkillType>> resultList
    ) {
        int damage = critical((int) (playerStatus.getDamage() * 2.7), playerStatus.getCriticalDamage(), playerStatus.getCriticalChance());
        int damageReceived = Math.max(0, damage - monsterStatus.getDefence());
        monsterStatus.setHealth(Math.max(0, monsterStatus.getHealth() - damageReceived));

        int healing = (int) (damageReceived * 0.8);
        playerStatus.setHealth(Math.min(playerOriginalStatus.getHealth(), playerStatus.getHealth() + healing));

        Light light = (Light) playerCondition.get("light");
        light.unapply(playerStatus, playerOriginalStatus, 2);

        if (light.getTurn() == 0) {
            playerCondition.remove(light.getId());
        }

        resultList.add(new Pair<>(String.valueOf(damageReceived), SkillType.ATTACK));
        resultList.add(new Pair<>(String.valueOf(healing), SkillType.HEAL));
    }

    private void thirdType(
            PlayerStatus playerStatus,
            PlayerOriginalStatus playerOriginalStatus,
            Map<String, Condition> playerCondition,
            MonsterStatus monsterStatus,
            List<Pair<String, SkillType>> resultList
    ) {
        int damage = critical((int) (playerStatus.getDamage() * 5.5), playerStatus.getCriticalDamage(), playerStatus.getCriticalChance());
        int damageReceived = Math.max(0, damage - monsterStatus.getDefence());
        monsterStatus.setHealth(Math.max(0, monsterStatus.getHealth() - damageReceived));

        Light light = (Light) playerCondition.get("light");
        light.unapply(playerStatus, playerOriginalStatus, 4);
        playerCondition.remove(light.getId());

        resultList.add(new Pair<>(String.valueOf(damageReceived), SkillType.ATTACK));
    }

    private void fourthType(
            PlayerStatus playerStatus,
            PlayerOriginalStatus playerOriginalStatus,
            Map<String, Condition> playerCondition,
            MonsterStatus monsterStatus,
            List<Pair<String, SkillType>> resultList
    ) {
        int damage = critical((int) (playerStatus.getDamage() * 7.77), playerStatus.getCriticalDamage(), playerStatus.getCriticalChance());
        int damageReceived = Math.max(0, damage - monsterStatus.getDefence());
        monsterStatus.setHealth(Math.max(0, monsterStatus.getHealth() - damageReceived));

        Light light = (Light) playerCondition.get("light");
        light.unapply(playerStatus, playerOriginalStatus, 5);
        playerCondition.remove(light.getId());

        AngelsProtection angelsProtection = new AngelsProtection();
        playerCondition.put(angelsProtection.getId(), angelsProtection);

        resultList.add(new Pair<>(String.valueOf(damageReceived), SkillType.ATTACK));
        resultList.add(new Pair<>(angelsProtection.getName(), SkillType.BUFF));
    }
}
