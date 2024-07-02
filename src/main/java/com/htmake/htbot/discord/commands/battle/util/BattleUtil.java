package com.htmake.htbot.discord.commands.battle.util;

import com.htmake.htbot.discord.skillAction.condition.Condition;
import com.htmake.htbot.discord.commands.dungeon.data.GetItem;
import com.htmake.htbot.discord.skillAction.condition.extend.Faint;
import com.htmake.htbot.discord.util.FormatUtil;
import com.htmake.htbot.discord.util.MessageUtil;
import com.htmake.htbot.discord.util.ObjectMapperUtil;
import com.htmake.htbot.global.cache.CacheFactory;
import com.htmake.htbot.discord.commands.battle.cache.MonsterDataCache;
import com.htmake.htbot.discord.commands.battle.cache.PlayerDataCache;
import com.htmake.htbot.discord.commands.battle.cache.SituationCache;
import com.htmake.htbot.discord.commands.battle.data.status.extend.MonsterStatus;
import com.htmake.htbot.discord.commands.battle.data.status.extend.PlayerStatus;
import com.htmake.htbot.discord.commands.battle.data.Situation;
import com.htmake.htbot.global.unirest.HttpClient;
import com.htmake.htbot.global.unirest.impl.HttpClientImpl;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import kotlin.Pair;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.awt.*;
import java.util.*;
import java.util.List;

public class BattleUtil {

    private final MessageUtil messageUtil;
    private final HttpClient httpClient;
    private final ObjectMapperUtil objectMapperUtil;

    private final PlayerDataCache playerDataCache;
    private final MonsterDataCache monsterDataCache;
    private final SituationCache situationCache;

    public BattleUtil() {
        this.messageUtil = new MessageUtil();
        this.httpClient = new HttpClientImpl();
        this.objectMapperUtil = new ObjectMapperUtil();

        this.playerDataCache = CacheFactory.playerDataCache;
        this.monsterDataCache = CacheFactory.monsterDataCache;
        this.situationCache = CacheFactory.situationCache;
    }

    public void updateSituation(String playerId, String message) {
        Situation situation = situationCache.get(playerId);
        List<String> messageList = situation.getMessageList();

        if (messageList.size() >= 7) {
            messageList.remove(0);
        }
        messageList.add(message);

        situation.setMessageList(messageList);
        situationCache.put(playerId, situation);
    }

    public void editEmbed(ButtonInteractionEvent event, PlayerStatus playerStatus, MonsterStatus monsterStatus, String type) {
        MessageEmbed embed = event.getMessage().getEmbeds().get(0);

        String monsterInfo = "Lv." + monsterStatus.getLevel() + " " + monsterStatus.getName();
        String playerInfo = "Lv." + playerStatus.getLevel() + " " + playerStatus.getName();

        String situation = situationFormat(event.getUser().getId());

        MessageEmbed newEmbed = new EmbedBuilder()
                .setColor(Color.GREEN)
                .setAuthor(embed.getAuthor().getName(), null, embed.getAuthor().getIconUrl())
                .setTitle(embed.getTitle())

                .addField(monsterInfo, conditionFormat(monsterStatus.getConditionMap()), false)
                .addField(":crossed_swords: 공격력", "" + FormatUtil.decimalFormat(monsterStatus.getDamage()), true)
                .addField(":heart: 체력", "" + FormatUtil.decimalFormat(monsterStatus.getHealth()), true)
                .addField(":shield: 방어력", "" + FormatUtil.decimalFormat(monsterStatus.getDefence()), true)

                .addField(":video_game: 전투 현황", situation, false)

                .addField(":crossed_swords: 공격력", "" + FormatUtil.decimalFormat(playerStatus.getDamage()), true)
                .addField(":heart: 체력", "" + FormatUtil.decimalFormat(playerStatus.getHealth()), true)
                .addField(":shield: 방어력", "" + FormatUtil.decimalFormat(playerStatus.getDefence()), true)
                .addField(":large_blue_diamond: 마나", "" + FormatUtil.decimalFormat(playerStatus.getMana()), true)
                .addField(":boom: 치명타 확률", playerStatus.getCriticalChance() + "%", true)
                .addField(":boom: 치명타 데미지", FormatUtil.decimalFormat(playerStatus.getCriticalDamage()) + "%", true)
                .addField(playerInfo, conditionFormat(playerStatus.getConditionMap()), false)
                .build();

        if (type.equals("progress")) {
            event.getHook().editOriginalEmbeds(newEmbed).queue();
        } else {
            event.getHook().editOriginalEmbeds(newEmbed)
                    .setActionRow(getButtonList(type))
                    .queue();
        }
    }

    private String situationFormat(String id) {
        Situation situation = situationCache.get(id);
        List<String> messageList = situation.getMessageList();

        StringBuilder sb = new StringBuilder();

        sb.append("```");
        for (int i = 0; i < 7; i++) {
            if (i < messageList.size()) {
                sb.append("| ").append(messageList.get(i)).append("\n");
            } else {
                sb.append("|\n");
            }
        }
        sb.append("```");

        return sb.toString();
    }

    private String conditionFormat(Map<String, Condition> conditionMap) {
        String formattedCondition = "";

        for (Map.Entry<String, Condition> entry : conditionMap.entrySet()) {
            Condition condition = entry.getValue();
            formattedCondition += condition.getEmoji() + turnFormat(condition.getTurn()) + " ";
        }

        return formattedCondition;
    }

    private String turnFormat(int turn) {
        return switch (turn) {
            case 1 -> "¹";
            case 2 -> "²";
            case 3 -> "³";
            case 4 -> "⁴";
            case 5 -> "⁵";
            case 6 -> "⁶";
            case 7 -> "⁷";
            case 8 -> "⁸";
            case 9 -> "⁹";
            default -> "";
        };
    }

    private List<Button> getButtonList(String type) {
        List<Button> buttonList = new ArrayList<>();

        switch (type) {
            case "start" -> {
                buttonList.add(Button.success("battle-attack", "공격").asDisabled());
                buttonList.add(Button.primary("battle-skill-open", "스킬").asDisabled());
            }
            case "end" -> {
                buttonList.add(Button.success("battle-attack", "공격"));
                buttonList.add(Button.primary("battle-skill-open", "스킬"));
            }
        }

        buttonList.add(Button.danger("battle-retreat", "후퇴"));

        return buttonList;
    }

    public void removeCurrentBattleCache(String playerId) {
        playerDataCache.remove(playerId);
        monsterDataCache.remove(playerId);
        situationCache.remove(playerId);
    }

    public MessageEmbed battleDefeat(String playerId) {
        messageUtil.remove(playerId);

        return new EmbedBuilder()
                .setColor(Color.RED)
                .setTitle(":skull: 전투 패배")
                .setDescription("전투에서 패배했습니다.")
                .build();
    }

    public HttpResponse<JsonNode> insertGetItemList(List<GetItem> getItemList, String playerId) {
        Map<String, Object> requestData = new HashMap<>();
        requestData.put("dropItemList", getItemList);

        String endPoint = "/inventory/insert/dropItem/{player_id}";
        Pair<String, String> routeParam = new Pair<>("player_id", playerId);
        String requestBody = objectMapperUtil.mapper(requestData);

        return httpClient.sendPostRequest(endPoint, routeParam, requestBody);
    }

    public boolean conditionCheck(ButtonInteractionEvent event, PlayerStatus playerStatus, MonsterStatus monsterStatus) {
        Map<String, Condition> playerCondition = playerStatus.getConditionMap();

        if (playerCondition.containsKey("faint")) {
            Faint faint = (Faint) playerCondition.get("faint");

            if (faint.applyEffect()) {
                String message = event.getUser().getName() + "이/가 기절했다.";
                updateSituation(event.getUser().getId(), message);
                editEmbed(event, playerStatus, monsterStatus, "start");

                return true;
            }
        }

        return false;
    }
}
