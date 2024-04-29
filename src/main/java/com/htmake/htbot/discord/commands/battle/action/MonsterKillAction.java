package com.htmake.htbot.discord.commands.battle.action;

import com.htmake.htbot.discord.commands.battle.data.MonsterStatus;
import com.htmake.htbot.discord.commands.battle.data.PlayerStatus;
import com.htmake.htbot.discord.commands.battle.util.BattleUtil;
import com.htmake.htbot.global.unirest.HttpClient;
import com.htmake.htbot.global.unirest.impl.HttpClientImpl;
import kotlin.Pair;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

public class MonsterKillAction {

    private final HttpClient httpClient;
    private final BattleUtil battleUtil;
    private final BattleResultAction battleResultAction;

    public MonsterKillAction() {
        this.httpClient = new HttpClientImpl();
        this.battleUtil = new BattleUtil();
        this.battleResultAction = new BattleResultAction();
    }

    public void execute(ButtonInteractionEvent event, PlayerStatus playerStatus, MonsterStatus monsterStatus) {
        killMonster(event, playerStatus, monsterStatus);
        countMonster(event, monsterStatus);
    }

    private void killMonster(ButtonInteractionEvent event, PlayerStatus playerStatus, MonsterStatus monsterStatus) {
        String playerId = event.getUser().getId();

        String message = monsterStatus.getName() + "을/를 처치했다!";
        battleUtil.updateSituation(playerId, message);
        battleUtil.editEmbed(event, playerStatus, monsterStatus, "progress");

        battleResultAction.execute(event, monsterStatus.getId());

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
