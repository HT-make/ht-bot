package com.htmake.htbot.discord.commands.battle.action;

import com.htmake.htbot.discord.commands.battle.data.MonsterStatus;
import com.htmake.htbot.discord.commands.battle.data.PlayerStatus;
import com.htmake.htbot.discord.commands.battle.util.BattleUtil;
import com.htmake.htbot.discord.commands.dungeon.cache.DungeonTypeCache;
import com.htmake.htbot.discord.commands.dungeon.enums.DungeonType;
import com.htmake.htbot.discord.util.ObjectMapperUtil;
import com.htmake.htbot.global.cache.CacheFactory;
import com.htmake.htbot.global.unirest.HttpClient;
import com.htmake.htbot.global.unirest.impl.HttpClientImpl;
import kotlin.Pair;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

import java.util.HashMap;
import java.util.Map;

public class MonsterKillAction {

    private final HttpClient httpClient;
    private final BattleUtil battleUtil;
    private final ObjectMapperUtil objectMapperUtil;

    private final FieldBattleResultAction fieldBattleResultAction;
    private final BossBattleResultAction bossBattleResultAction;

    private final DungeonTypeCache dungeonTypeCache;

    public MonsterKillAction() {
        this.httpClient = new HttpClientImpl();
        this.battleUtil = new BattleUtil();
        this.objectMapperUtil = new ObjectMapperUtil();

        this.fieldBattleResultAction = new FieldBattleResultAction();
        this.bossBattleResultAction = new BossBattleResultAction();

        this.dungeonTypeCache = CacheFactory.dungeonTypeCache;
    }

    public void execute(ButtonInteractionEvent event, PlayerStatus playerStatus, MonsterStatus monsterStatus) {
        String playerId = event.getUser().getId();

        String message = monsterStatus.getName() + "을/를 처치했다!";
        battleUtil.updateSituation(playerId, message);
        battleUtil.editEmbed(event, playerStatus, monsterStatus, "progress");

        DungeonType dungeonType = dungeonTypeCache.get(playerId);
        switch (dungeonType) {
            case FIELD -> fieldBattleResultAction.execute(event, monsterStatus.getId());
            case BOSS -> bossBattleResultAction.execute(event, monsterStatus.getId());
        }

        battleUtil.removeCurrentBattleCache(playerId);

        questMonsterCount(playerId, monsterStatus.getName());
    }

    private void questMonsterCount(String playerId, String monsterName) {
        Map<String, Object> requestData = new HashMap<>();
        requestData.put("name", monsterName);

        String endPoint = "/quest/monster/{player_id}";
        Pair<String, String> routeParam = new Pair<>("player_id", playerId);
        String jsonBody = objectMapperUtil.mapper(requestData);

        httpClient.sendPatchRequest(endPoint, routeParam, jsonBody);
    }
}
