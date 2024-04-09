package com.htmake.htbot.discord.commands.battle.event;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.htmake.htbot.cache.Caches;
import com.htmake.htbot.discord.commands.battle.cache.MonsterStatusCache;
import com.htmake.htbot.discord.commands.battle.cache.PlayerStatusCache;
import com.htmake.htbot.discord.commands.battle.cache.SituationCache;
import com.htmake.htbot.discord.commands.battle.data.MonsterStatus;
import com.htmake.htbot.discord.commands.battle.data.PlayerStatus;
import com.htmake.htbot.discord.commands.battle.data.Situation;
import com.htmake.htbot.domain.dungeon.enums.DungeonEnum;
import com.htmake.htbot.unirest.HttpClient;
import com.htmake.htbot.unirest.impl.HttpClientImpl;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import kotlin.Pair;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.json.JSONArray;
import org.json.JSONObject;

import java.awt.*;
import java.util.*;
import java.util.List;

@Slf4j
public class BattleAttackEvent {

    private final HttpClient httpClient;

    private final PlayerStatusCache playerStatusCache;

    private final MonsterStatusCache monsterStatusCache;

    private final SituationCache situationCache;

    public BattleAttackEvent() {
        this.httpClient = new HttpClientImpl();
        this.playerStatusCache = Caches.playerStatusCache;
        this.monsterStatusCache = Caches.monsterStatusCache;
        this.situationCache = Caches.situationCache;
    }

    public void execute(ButtonInteractionEvent event) {
        String playerId = event.getUser().getId();
        PlayerStatus playerStatus = playerStatusCache.get(playerId);
        MonsterStatus monsterStatus = monsterStatusCache.get(playerId);

        playerTurn(event, playerStatus, monsterStatus);

        if (monsterStatus.getHealth() == 0) {
            killMonster(event, playerStatus, monsterStatus);
            return;
        }

        monsterTurn(event, playerStatus, monsterStatus);

        if (playerStatus.getHealth() == 0) {
            killPlayer(event, playerStatus, monsterStatus);
        }
    }

    private void updateSituation(String playerId, String message) {
        Situation situation = situationCache.get(playerId);
        List<String> messageList = situation.getMessageList();

        if (messageList.size() >= 5) {
            messageList.remove(0);
        }
        messageList.add(message);

        situation.setMessageList(messageList);
        situationCache.put(playerId, situation);
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

        updateSituation(playerId, message);

        editEmbed(event, playerStatus, monsterStatus);

        monsterStatus.setHealth(Math.max(0, (monsterStatus.getHealth() - damage.getFirst())));
        message = damage.getFirst() + "의 데미지를 입혔다!";

        updateSituation(playerId, message);

        editEmbed(event, playerStatus, monsterStatus);
    }

    private void monsterTurn(ButtonInteractionEvent event, PlayerStatus playerStatus, MonsterStatus monsterStatus) {
        String playerId = event.getUser().getId();

        String message = monsterStatus.getName() + "의 공격.";

        updateSituation(playerId, message);

        editEmbed(event, playerStatus, monsterStatus);

        int damage = Math.max(0, monsterStatus.getDamage() - playerStatus.getDefence());
        playerStatus.setHealth(Math.max(0, playerStatus.getHealth() - damage));
        message = damage + "의 데미지를 입혔다!";

        updateSituation(playerId, message);

        editEmbed(event, playerStatus, monsterStatus);
    }

    private void editEmbed(ButtonInteractionEvent event, PlayerStatus playerStatus, MonsterStatus monsterStatus) {
        MessageEmbed embed = event.getMessage().getEmbeds().get(0);

        Situation situation = situationCache.get(event.getUser().getId());
        List<String> messageList = situation.getMessageList();

        StringBuilder sb = new StringBuilder();
        for (String message : messageList) {
            sb.append("\n").append(message);
        }

        MessageEmbed newEmbed = new EmbedBuilder()
                .setColor(Color.GREEN)
                .setTitle(embed.getTitle())
                .setDescription(embed.getDescription())

                .addField(":crossed_swords: 공격력", "" + monsterStatus.getDamage(), true)
                .addField(":heart: 체력", "" + monsterStatus.getHealth(), true)
                .addField(":shield: 방어력", "" + monsterStatus.getDefence(), true)

                .addField(":video_game: 전투 현황", "" + sb, false)
                .addBlankField(false)

                .addField(":crossed_swords: 공격력", "" + playerStatus.getDamage(), true)
                .addField(":heart: 체력", "" + playerStatus.getHealth(), true)
                .addField(":shield: 방어력", "" + playerStatus.getDamage(), true)

                .addField(":large_blue_diamond: 마나", "" + playerStatus.getMana(), true)
                .addField(":boom: 치명타 확률", playerStatus.getCriticalChance() + "%", true)
                .addField(":boom: 치명타 데미지", playerStatus.getCriticalDamage() + "%", true)

                .setFooter("" + embed.getFooter().getText())
                .build();

        event.getMessage().editMessageEmbeds(newEmbed).queue();
    }

    private void killMonster(ButtonInteractionEvent event, PlayerStatus playerStatus, MonsterStatus monsterStatus) {
        String playerId = event.getUser().getId();

        String message = monsterStatus.getName() + "을/를 처치했다!";
        updateSituation(playerId, message);

        editEmbed(event, playerStatus, monsterStatus);

        getAward(event, monsterStatus.getId());

        removeCache(playerId);
    }

    private void getAward(ButtonInteractionEvent event, String monsterId) {
        ObjectMapper objectMapper = new ObjectMapper();

        JSONObject monsterLoot = getMonsterLoot(event, monsterId);

        Map<String, Object> requestData = new HashMap<>();

        requestData.put("exp", monsterLoot.getInt("exp"));
        requestData.put("gold", monsterLoot.getInt("gold"));
        requestData.put("getItemList", getItemList(monsterLoot));

        String endPoint = "/player/battle/win/{player_id}";
        Pair<String, String> routeParam = new Pair<>("player_id", event.getUser().getId());
        String jsonBody;

        try {
            jsonBody = objectMapper.writeValueAsString(requestData);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        HttpResponse<JsonNode> response = httpClient.sendPatchRequest(endPoint, routeParam, jsonBody);

        if (response.getStatus() == 200) {
            battleResult(event, response.getBody().getObject(), monsterLoot);
        } else {
            log.error(String.valueOf(response.getBody()));
            battleError(event);
        }
    }

    private void killPlayer(ButtonInteractionEvent event, PlayerStatus playerStatus, MonsterStatus monsterStatus) {

        User user = event.getUser();
        String playerId = user.getId();
        String name = user.getName();

        String message = name + "이/가 사망했다.";
        updateSituation(playerId, message);

        editEmbed(event, playerStatus, monsterStatus);

        event.getMessage().editMessageComponents(Collections.emptyList()).queue();
        event.getMessage().editMessageEmbeds(new EmbedBuilder()
                        .setColor(Color.RED)
                        .setTitle(":skull: 전투 패배")
                        .setDescription("전투에서 패배했습니다.")
                        .build()
                )
                .queue();

        removeCache(playerId);
    }

    private JSONObject getMonsterLoot(ButtonInteractionEvent event, String monsterId) {
        String endPoint = "/dungeon/monster/{monster_id}";
        Pair<String, String> routeParam = new Pair<>("monster_id", monsterId);

        HttpResponse<JsonNode> response = httpClient.sendGetRequest(endPoint, routeParam);

        if (response.getStatus() == 200) {
            return response.getBody().getObject();
        }

        log.error(String.valueOf(response.getBody()));
        battleError(event);

        return null;
    }

    private List<Map<String, String>> getItemList(JSONObject monsterLoot) {
        JSONArray dropItems = monsterLoot.getJSONArray("dropItemList");

        List<Map<String, String>> getItemList = new ArrayList<>();

        for (int i = 0; i < dropItems.length(); i++) {
            JSONObject dropItemObject = dropItems.getJSONObject(i);

            Random random = new Random();
            int randomNum = random.nextInt(100);

            int dropChance = dropItemObject.getInt("chance");

            if (randomNum < dropChance) {
                Map<String, String> item = new HashMap<>();
                item.put("id", dropItemObject.getString("id"));
                item.put("name", dropItemObject.getString("name"));
                getItemList.add(item);
            }
        }

        return getItemList;
    }

    private void battleResult(ButtonInteractionEvent event, JSONObject levelUp, JSONObject monsterLoot) {
        Message message = event.getMessage();
        MessageEmbed embed = message.getEmbeds().get(0);

        List<String> title = List.of(embed.getTitle().split("-"));
        String nextDungeon = "enter-";

        for (DungeonEnum dungeon : DungeonEnum.values()) {
            if (dungeon.getName().equals(title.get(0))) {
                nextDungeon += dungeon.getValue();
                break;
            }
        }

        int stage = Integer.parseInt(title.get(1)) + 1;
        nextDungeon += "-" + stage;

        String levelUpMessage = levelUp.getBoolean("levelUp") ? ":up: 레벨업!!" : "";

        String getItem = "";

        List<Map<String, String>> getItemList = getItemList(monsterLoot);

        if (getItemList.size() > 0) {
            for (Map<String, String> item : getItemList) {
                Set<String> keySet = item.keySet();
                for (String key : keySet) {
                    if (key.equals("name")) getItem += item.get(key) + "\n";
                }
            }
        } else {
            getItem = "획득한 아이템이 없습니다.";
        }

        MessageEmbed newEmbed = new EmbedBuilder()
                .setColor(Color.GREEN)
                .setTitle(":crossed_swords: 전투 승리!")
                .setDescription(levelUpMessage)
                .addField(":sparkles: 획득 경험치", "" + monsterLoot.getInt("exp"), true)
                .addField(":coin: 획득 골드", "" + monsterLoot.get("gold"), true)
                .addField(":purse: 획득 아이템", getItem, false)
                .build();

        message.editMessageEmbeds(newEmbed)
                .setActionRow(
                        Button.success(nextDungeon, "전진하기"),
                        Button.danger("close-dungeon", "돌아가기")
                )
                .queue();
    }

    private void removeCache(String playerId) {
        playerStatusCache.remove(playerId);
        monsterStatusCache.remove(playerId);
        situationCache.remove(playerId);
    }

    private void battleError(ButtonInteractionEvent event) {
        event.getMessage().editMessageComponents(Collections.emptyList()).queue();
        event.getMessage().editMessageEmbeds(new EmbedBuilder()
                        .setColor(Color.ORANGE)
                        .setTitle(":warning: 전투 오류")
                        .setDescription("전투 처리를 실패했습니다.")
                        .build()
                )
                .queue();
    }
}
