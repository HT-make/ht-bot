package com.htmake.htbot.discord.commands.battle.util;

import com.htmake.htbot.discord.commands.dungeon.data.GetItem;
import com.htmake.htbot.discord.util.MessageUtil;
import com.htmake.htbot.discord.util.ObjectMapperUtil;
import com.htmake.htbot.global.cache.CacheFactory;
import com.htmake.htbot.discord.commands.battle.cache.MonsterStatusCache;
import com.htmake.htbot.discord.commands.battle.cache.PlayerStatusCache;
import com.htmake.htbot.discord.commands.battle.cache.SituationCache;
import com.htmake.htbot.discord.commands.battle.data.MonsterStatus;
import com.htmake.htbot.discord.commands.battle.data.PlayerStatus;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BattleUtil {

    private final MessageUtil messageUtil;
    private final HttpClient httpClient;
    private final ObjectMapperUtil objectMapperUtil;

    private final PlayerStatusCache playerStatusCache;
    private final MonsterStatusCache monsterStatusCache;
    private final SituationCache situationCache;

    public BattleUtil() {
        this.messageUtil = new MessageUtil();
        this.httpClient = new HttpClientImpl();
        this.objectMapperUtil = new ObjectMapperUtil();

        this.playerStatusCache = CacheFactory.playerStatusCache;
        this.monsterStatusCache = CacheFactory.monsterStatusCache;
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

        Situation situation = situationCache.get(event.getUser().getId());
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

        MessageEmbed newEmbed = new EmbedBuilder()
                .setColor(Color.GREEN)
                .setAuthor(embed.getAuthor().getName(), null, embed.getAuthor().getIconUrl())
                .setTitle(embed.getTitle())
                .setDescription(embed.getDescription())

                .addField(":crossed_swords: 공격력", "" + monsterStatus.getDamage(), true)
                .addField(":heart: 체력", "" + monsterStatus.getHealth(), true)
                .addField(":shield: 방어력", "" + monsterStatus.getDefence(), true)

                .addField(":video_game: 전투 현황", sb.toString(), false)
                .addBlankField(false)

                .addField(":crossed_swords: 공격력", "" + playerStatus.getDamage(), true)
                .addField(":heart: 체력", "" + playerStatus.getHealth(), true)
                .addField(":shield: 방어력", "" + playerStatus.getDefence(), true)

                .addField(":large_blue_diamond: 마나", "" + playerStatus.getMana(), true)
                .addField(":boom: 치명타 확률", playerStatus.getCriticalChance() + "%", true)
                .addField(":boom: 치명타 데미지", playerStatus.getCriticalDamage() + "%", true)

                .setFooter(embed.getFooter().getText())
                .build();

        if (type.equals("progress")) {
            event.getHook().editOriginalEmbeds(newEmbed).queue();
        } else {
            event.getHook().editOriginalEmbeds(newEmbed)
                    .setActionRow(getButtonList(type))
                    .queue();
        }
    }

    private List<Button> getButtonList(String type) {
        List<Button> buttonList = new ArrayList<>();

        switch (type) {
            case "start" -> {
                buttonList.add(Button.success("battle-attack", "공격").asDisabled());
                buttonList.add(Button.primary("battle-skill-open", "스킬").asDisabled());
                buttonList.add(Button.primary("battle-potion-open", "포션").asDisabled());
            }
            case "end" -> {
                buttonList.add(Button.success("battle-attack", "공격"));
                buttonList.add(Button.primary("battle-skill-open", "스킬"));
                buttonList.add(Button.primary("battle-potion-open", "포션"));
            }
        }

        buttonList.add(Button.danger("battle-retreat", "후퇴"));

        return buttonList;
    }

    public void removeCurrentBattleCache(String playerId) {
        playerStatusCache.remove(playerId);
        monsterStatusCache.remove(playerId);
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
}
