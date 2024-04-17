package com.htmake.htbot.discord.commands.battle.event;

import com.htmake.htbot.global.cache.Caches;
import com.htmake.htbot.discord.commands.battle.cache.MonsterStatusCache;
import com.htmake.htbot.discord.commands.battle.cache.PlayerStatusCache;
import com.htmake.htbot.discord.commands.battle.data.MonsterStatus;
import com.htmake.htbot.discord.commands.battle.data.PlayerStatus;
import com.htmake.htbot.discord.commands.battle.util.BattleUtil;
import com.htmake.htbot.global.unirest.HttpClient;
import com.htmake.htbot.global.unirest.impl.HttpClientImpl;
import kotlin.Pair;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

import java.util.*;

@Slf4j
public class PlayerAttackEvent {

    private final HttpClient httpClient;

    private final BattleUtil battleUtil;
    private final MonsterAttackEvent monsterAttackEvent;
    private final GetAwardEvent getAwardEvent;

    private final PlayerStatusCache playerStatusCache;
    private final MonsterStatusCache monsterStatusCache;

    public PlayerAttackEvent() {
        this.httpClient = new HttpClientImpl();

        this.battleUtil = new BattleUtil();
        this.monsterAttackEvent = new MonsterAttackEvent();
        this.getAwardEvent = new GetAwardEvent();

        this.playerStatusCache = Caches.playerStatusCache;
        this.monsterStatusCache = Caches.monsterStatusCache;
    }

    public void execute(ButtonInteractionEvent event) {
        String playerId = event.getUser().getId();
        PlayerStatus playerStatus = playerStatusCache.get(playerId);
        MonsterStatus monsterStatus = monsterStatusCache.get(playerId);

        playerTurn(event, playerStatus, monsterStatus);

        if (monsterStatus.getHealth() == 0) {
            killMonster(event, playerStatus, monsterStatus);
            countMonster(event, monsterStatus);
        } else {
            monsterAttackEvent.execute(event, playerStatus, monsterStatus);
        }
    }

    private Pair<Integer, Boolean> playerAttackDamage(PlayerStatus playerStatus, MonsterStatus monsterStatus) {
        Random random = new Random();
        int randomNum = random.nextInt(100);

        int damage = playerStatus.getDamage();
        int criticalChance = playerStatus.getCriticalChance();
        int criticalDamage = playerStatus.getCriticalDamage();

        int monsterDefence = monsterStatus.getDefence();

        if (randomNum < criticalChance) {
            double criticalDamageMultiple = (double) criticalDamage / 100;
            damage = (int) (damage * criticalDamageMultiple) - monsterDefence;

            return new Pair<>(Math.max(damage, 0), true);
        }

        damage -= monsterDefence;
        return new Pair<>(Math.max(damage, 0), false);
    }

    private void playerTurn(ButtonInteractionEvent event, PlayerStatus playerStatus, MonsterStatus monsterStatus) {
        Pair<Integer, Boolean> damage = playerAttackDamage(playerStatus, monsterStatus);

        User user = event.getUser();
        String playerId = user.getId();
        String name = user.getName();

        String message = name + "의 ";

        if (damage.getSecond()) {
            message += "치명타 공격!";
        } else {
            message += "공격.";
        }

        battleUtil.updateSituation(playerId, message);

        battleUtil.editEmbed(event, playerStatus, monsterStatus);

        monsterStatus.setHealth(Math.max(0, (monsterStatus.getHealth() - damage.getFirst())));

        message = damage.getFirst() + "의 데미지를 입혔다!";
        battleUtil.updateSituation(playerId, message);

        battleUtil.editEmbed(event, playerStatus, monsterStatus);
    }

    private void killMonster(ButtonInteractionEvent event, PlayerStatus playerStatus, MonsterStatus monsterStatus) {
        String playerId = event.getUser().getId();

        String message = monsterStatus.getName() + "을/를 처치했다!";
        battleUtil.updateSituation(playerId, message);

        battleUtil.editEmbed(event, playerStatus, monsterStatus);

        getAwardEvent.execute(event, monsterStatus.getId());

        battleUtil.removeCurrentBattleCache(playerId);
    }

    private void countMonster(ButtonInteractionEvent event, MonsterStatus monsterStatus) {
        String playerId = event.getUser().getId();

        String endPoint = "/quest/monster/{player_id}";
        Pair<String, String> routeParam = new Pair<>("player_id", playerId);

        String requestBody = "{\"name\": \"" + monsterStatus.getName() + "\"}";

        httpClient.sendPatchRequest(endPoint, routeParam, requestBody);
    }
}
