package com.htmake.htbot.discord.skillAction.skills.archer.class4;

import com.htmake.htbot.discord.commands.battle.data.MonsterData;
import com.htmake.htbot.discord.commands.battle.data.PlayerData;
import com.htmake.htbot.discord.commands.battle.data.status.extend.MonsterStatus;
import com.htmake.htbot.discord.commands.battle.data.status.extend.PlayerOriginalStatus;
import com.htmake.htbot.discord.commands.battle.data.status.extend.PlayerStatus;
import com.htmake.htbot.discord.skillAction.condition.Condition;
import com.htmake.htbot.discord.skillAction.condition.extend.etc.DivineBeast;
import com.htmake.htbot.discord.skillAction.skill.impl.AbstractSkillStrategy;
import com.htmake.htbot.discord.skillAction.type.SkillType;
import kotlin.Pair;

import java.util.List;
import java.util.Map;

public class S2403 extends AbstractSkillStrategy {

    @Override
    protected int getManaCost() {
        return 150;
    }

    @Override
    protected void applySkill(PlayerData playerData, MonsterData monsterData, List<Pair<String, SkillType>> resultList) {
        PlayerStatus playerStatus = playerData.getPlayerStatus();
        PlayerOriginalStatus playerOriginalStatus = playerData.getPlayerOriginalStatus();
        Map<String, Condition> playerCondition = playerData.getPlayerStatus().getConditionMap();
        MonsterStatus monsterStatus = monsterData.getMonsterStatus();

        int damage = critical((int) (playerStatus.getDamage() * 3.5), playerStatus.getCriticalDamage(), playerStatus.getCriticalChance(), playerStatus.getJob());
        int damageReceived = Math.max(0, damage - monsterStatus.getDefence());
        monsterStatus.setHealth(Math.max(0, monsterStatus.getHealth() - damageReceived));

        if (playerCondition.containsKey("divine_beast")) {
            DivineBeast existsDivineBeast = (DivineBeast) playerCondition.get("divine_beast");
            existsDivineBeast.unapply(playerStatus, playerOriginalStatus);
        }

        DivineBeast divineBeast = new DivineBeast();
        divineBeast.apply(playerStatus, playerOriginalStatus);
        playerCondition.put(divineBeast.getId(), divineBeast);

        resultList.add(new Pair<>(String.valueOf(damageReceived), SkillType.ATTACK));
        resultList.add(new Pair<>(divineBeast.getName(), SkillType.BUFF));
    }
}
