package com.htmake.htbot.discord.commands.quest.event;

import com.htmake.htbot.discord.commands.quest.action.QuestDetailAction;
import com.htmake.htbot.discord.commands.quest.action.QuestDialogueAction;
import com.htmake.htbot.discord.util.ErrorUtil;
import com.htmake.htbot.global.unirest.HttpClient;
import com.htmake.htbot.global.unirest.impl.HttpClientImpl;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import kotlin.Pair;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.json.JSONObject;

public class QuestSlashEvent {

    private final HttpClient httpClient;
    private final ErrorUtil errorUtil;

    private final QuestDetailAction questDetailAction;
    private final QuestDialogueAction questDialogueAction;

    public QuestSlashEvent() {
        this.httpClient = new HttpClientImpl();
        this.errorUtil = new ErrorUtil();

        this.questDetailAction = new QuestDetailAction();
        this.questDialogueAction = new QuestDialogueAction();
    }

    public void execute(SlashCommandInteractionEvent event) {
        HttpResponse<JsonNode> response = request(event.getUser().getId());

        if (response.getStatus() == 200) {
            JSONObject checkObject = response.getBody().getObject();
            requestSuccess(event, checkObject);
        } else {
            errorUtil.sendError(event, "퀘스트", "퀘스트를 불러올 수 없습니다.");
        }
    }

    private HttpResponse<JsonNode> request(String playerId) {
        String endPoint = "/quest/dialogue/check/{player_id}";
        Pair<String, String> routeParam = new Pair<>("player_id", playerId);
        return httpClient.sendGetRequest(endPoint, routeParam);
    }

    private void requestSuccess(SlashCommandInteractionEvent event, JSONObject checkObject) {
        boolean readDialogue = checkObject.getBoolean("readDialogue");

        if (readDialogue) {
            questDetailAction.execute(event);
        } else {
            questDialogueAction.execute(event);
        }
    }
}


