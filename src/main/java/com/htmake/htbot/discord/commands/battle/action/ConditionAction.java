package com.htmake.htbot.discord.commands.battle.action;

import com.htmake.htbot.discord.commands.battle.cache.MonsterDataCache;
import com.htmake.htbot.discord.commands.battle.cache.PlayerDataCache;
import com.htmake.htbot.discord.commands.battle.data.MonsterData;
import com.htmake.htbot.discord.commands.battle.data.PlayerData;
import com.htmake.htbot.discord.skillAction.condition.Condition;
import com.htmake.htbot.discord.skillAction.condition.extend.Buff;
import com.htmake.htbot.discord.skillAction.condition.extend.DamageOverTime;
import com.htmake.htbot.discord.commands.battle.data.status.BasicStatus;
import com.htmake.htbot.discord.commands.battle.data.status.extend.MonsterOriginalStatus;
import com.htmake.htbot.discord.commands.battle.data.status.extend.MonsterStatus;
import com.htmake.htbot.discord.commands.battle.data.status.extend.PlayerOriginalStatus;
import com.htmake.htbot.discord.commands.battle.data.status.extend.PlayerStatus;
import com.htmake.htbot.discord.commands.battle.util.BattleUtil;
import com.htmake.htbot.global.cache.CacheFactory;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

import java.util.Map;

public class ConditionAction {

    private final BattleUtil battleUtil;

    private final MonsterKillAction monsterKillAction;
    private final PlayerKillAction playerKillAction;

    private final PlayerDataCache playerDataCache;
    private final MonsterDataCache monsterDataCache;

    public ConditionAction() {
        this.battleUtil = new BattleUtil();

        this.monsterKillAction = new MonsterKillAction();
        this.playerKillAction = new PlayerKillAction();

        this.playerDataCache = CacheFactory.playerDataCache;
        this.monsterDataCache = CacheFactory.monsterDataCache;
    }

    public void execute(ButtonInteractionEvent event) {
        User user = event.getUser();

        PlayerData playerData = playerDataCache.get(user.getId());
        PlayerStatus playerStatus = playerData.getPlayerStatus();
        PlayerOriginalStatus playerOriginalStatus = playerData.getPlayerOriginalStatus();

        MonsterData monsterData = monsterDataCache.get(user.getId());
        MonsterStatus monsterStatus = monsterData.getMonsterStatus();
        MonsterOriginalStatus monsterOriginalStatus = monsterData.getMonsterOriginalStatus();

        processCondition(event, playerStatus, monsterStatus, user.getId(), monsterStatus.getConditionMap(), true);
        processCondition(event, playerStatus, monsterStatus, user.getId(), playerStatus.getConditionMap(), false);

        updateCondition(playerStatus, playerOriginalStatus, playerStatus.getConditionMap());
        updateCondition(monsterStatus, monsterOriginalStatus, monsterStatus.getConditionMap());

        battleUtil.editEmbed(event, playerStatus, monsterStatus, "end");
    }

    private void processCondition(ButtonInteractionEvent event, PlayerStatus playerStatus, MonsterStatus monsterStatus, String userId, Map<String, Condition> conditionMap, boolean isMonster) {
        for (Map.Entry<String, Condition> entry : conditionMap.entrySet()) {
            Condition condition = entry.getValue();

            if (condition instanceof DamageOverTime damageOverTime) {
                if (isMonster) {
                    damageOverTime.execute(monsterStatus);
                    if (monsterStatus.getHealth() == 0) monsterKillAction.execute(event, playerStatus, monsterStatus);
                } else {
                    damageOverTime.execute(playerStatus);
                    if (playerStatus.getHealth() == 0) playerKillAction.execute(event, playerStatus, monsterStatus);
                }

                updateSituation(userId, isMonster ? monsterStatus.getName() : event.getUser().getName(), damageOverTime.getName(), damageOverTime.getDamage());
                battleUtil.editEmbed(event, playerStatus, monsterStatus, "progress");
            }
        }
    }

    private void updateCondition(BasicStatus status, BasicStatus originalStatus, Map<String, Condition> conditionMap) {
        for (Map.Entry<String, Condition> entry : conditionMap.entrySet()) {
            Condition condition = entry.getValue();
            int turn = condition.getTurn();

            if (turn - 1 > 0) {
                if (condition.isCheck()) {
                    condition.setTurn(turn - 1);
                } else {
                    condition.setCheck(true);
                }
                conditionMap.put(entry.getKey(), condition);
            } else {
                if (condition instanceof Buff buff) {
                    buff.unapply(status, originalStatus);
                }

                conditionMap.remove(entry.getKey());
            }
        }
    }

    private void updateSituation(String id, String targetName, String effect, int damage) {
        String message = targetName + "이/가 " + effect + "(으)로 인해 " + damage + " 데미지를 입었다.";
        battleUtil.updateSituation(id, message);
    }
}
