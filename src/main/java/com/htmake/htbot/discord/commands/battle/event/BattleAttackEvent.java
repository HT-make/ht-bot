package com.htmake.htbot.discord.commands.battle.event;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.htmake.htbot.domain.dungeon.enums.DungeonEnum;
import com.htmake.htbot.unirest.HttpClient;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import kotlin.Pair;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.json.JSONArray;
import org.json.JSONObject;

import java.awt.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.List;

@Slf4j
public class BattleAttackEvent {

    private final HttpClient httpClient;

    public BattleAttackEvent(ButtonInteractionEvent event, HttpClient httpClient) {
        this.httpClient = httpClient;

        MessageEmbed embed = event.getMessage().getEmbeds().get(0);
        List<MessageEmbed.Field> fieldList = embed.getFields();

        ArrayList<Integer> monster = parseStats(fieldList.subList(0, 3));
        ArrayList<Integer> player = parseStats(fieldList.subList(5, fieldList.size()));

        Pair<Integer, Boolean> playerDamage = playerAttackDamage(player, monster);
        monster.set(1, Math.max(0, monster.get(1) - playerDamage.getFirst()));
        ArrayList<String> situation = playerTurn(event, monster, player, playerDamage);

        if (!situation.get(0).equals("game over")) {
            int monsterDamage = Math.max(monster.get(0) - player.get(2), 0);
            player.set(1, Math.max(0, player.get(1) - monsterDamage));
            monsterTurn(event, monster, player, monsterDamage, situation);
        }
    }

    private ArrayList<Integer> parseStats(List<MessageEmbed.Field> fieldList) {
        ArrayList<Integer> stats = new ArrayList<>();

        for (MessageEmbed.Field field : fieldList) {
            String value = Objects.requireNonNull(field.getValue()).replace("%", "");
            stats.add(Integer.valueOf(value));
        }

        return stats;
    }

    private Pair<Integer, Boolean> playerAttackDamage(ArrayList<Integer> player, ArrayList<Integer> monster) {
        Random random = new Random();
        int randomNum = random.nextInt(100);

        int damage = player.get(0);
        int criticalChance = player.get(4);

        if (randomNum < criticalChance) {
            double criticalDamage = Double.valueOf(player.get(5)) / 100;
            damage = (int) (damage * criticalDamage);
            damage -= monster.get(2);

            return new Pair<>(Math.max(damage, 0), true);
        }

        damage -= monster.get(2);
        return new Pair<>(Math.max(damage, 0), false);
    }

    private ArrayList<String> playerTurn(
            ButtonInteractionEvent event,
            ArrayList<Integer> monster,
            ArrayList<Integer> player,
            Pair<Integer, Boolean> damage
    ) {
        MessageEmbed embed = event.getMessage().getEmbeds().get(0);
        String[] playerInfo = embed.getFooter().getText().split(" ");
        String newMessage = playerInfo[1] + "의 ";

        if (damage.getSecond()) {
            newMessage += "치명타 공격!";
        } else {
            newMessage += "공격.";
        }

        String[] situation = embed.getFields().get(3).getValue().split("\n");
        ArrayList<String> newSituation = new ArrayList<>(Arrays.asList(situation));

        if (newSituation.size() > 5) newSituation.remove(0);
        newSituation.add(newMessage);

        editEmbed(event, monster, player, newSituation);

        newMessage = damage.getFirst() + "의 데미지를 입혔다!";

        if (newSituation.size() > 5) newSituation.remove(0);
        newSituation.add(newMessage);

        editEmbed(event, monster, player, newSituation);

        if (monster.get(1) == 0) {
            newMessage = getMonsterName(event) + "을/를 처치했다!";

            if (newSituation.size() > 5) newSituation.remove(0);
            newSituation.add(newMessage);

            editEmbed(event, monster, player, newSituation);

            monsterKill(event);
            return new ArrayList<>(List.of("game over"));
        }

        return newSituation;
    }

    private void monsterTurn(
            ButtonInteractionEvent event,
            ArrayList<Integer> monster,
            ArrayList<Integer> player,
            int monsterDamage,
            ArrayList<String> situation
    ) {
        String newMessage = getMonsterName(event) + "의 공격.";

        if (situation.size() > 5) situation.remove(0);
        situation.add(newMessage);

        editEmbed(event, monster, player, situation);

        newMessage = monsterDamage + "의 데미지를 입혔다!";

        if (situation.size() > 5) situation.remove(0);
        situation.add(newMessage);

        editEmbed(event, monster, player, situation);

        if (player.get(1) <= 0) {
            MessageEmbed embed = event.getMessage().getEmbeds().get(0);
            String[] playerInfo = embed.getFooter().getText().split(" ");
            newMessage = playerInfo[1] + "가 사망했다.";

            if (situation.size() > 5) situation.remove(0);
            situation.add(newMessage);

            editEmbed(event, monster, player, situation);

            playerKill(event);
        }
    }

    private String getMonsterName(ButtonInteractionEvent event) {
        MessageEmbed embed = event.getMessage().getEmbeds().get(0);
        String[] monsterInfo = embed.getDescription().split(" ");
        String monsterName = "";

        for (int i = 1; i < monsterInfo.length; i++) {
            monsterName += " " + monsterInfo[i];
        }

        return monsterName;
    }

    private void editEmbed(
            ButtonInteractionEvent event,
            ArrayList<Integer> monster,
            ArrayList<Integer> player,
            ArrayList<String> situation
    ) {
        MessageEmbed embed = event.getMessage().getEmbeds().get(0);

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < situation.size(); i++) {
            if (i != 0) sb.append("\n");
            sb.append(situation.get(i));
        }

        MessageEmbed newEmbed = new EmbedBuilder()
                .setColor(Color.GREEN)
                .setTitle(embed.getTitle())
                .setDescription(embed.getDescription())

                .addField(":crossed_swords: 공격력", "" + monster.get(0), true)
                .addField(":heart: 체력", "" + monster.get(1), true)
                .addField(":shield: 방어력", "" + monster.get(2), true)

                .addField(":video_game: 전투 현황", "" + sb, false)
                .addBlankField(false)

                .addField(":crossed_swords: 공격력", "" + player.get(0), true)
                .addField(":heart: 체력", "" + player.get(1), true)
                .addField(":shield: 방어력", "" + player.get(2), true)

                .addField(":large_blue_diamond: 마나", "" + player.get(3), true)
                .addField(":boom: 치명타 확률", player.get(4) + "%", true)
                .addField(":boom: 치명타 데미지", player.get(5) + "%", true)

                .setFooter("" + embed.getFooter().getText())
                .build();

        event.getMessage().editMessageEmbeds(newEmbed).queue();
    }

    private void monsterKill(ButtonInteractionEvent event) {
        ObjectMapper objectMapper = new ObjectMapper();

        JSONObject monsterLoot = getMonsterLoot(event);

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

    private void playerKill(ButtonInteractionEvent event) {
        event.getMessage().editMessageComponents(Collections.emptyList()).queue();
        event.getMessage().editMessageEmbeds(new EmbedBuilder()
                        .setColor(Color.RED)
                        .setTitle(":skull: 전투 패배")
                        .setDescription("전투에서 패배했습니다.")
                        .build()
                )
                .queue();
    }

    private JSONObject getMonsterLoot(ButtonInteractionEvent event) {
        String endPoint = "/dungeon/monster/{monster_name}";
        String encodedMonsterName = URLEncoder.encode(getMonsterName(event), StandardCharsets.UTF_8);
        Pair<String, String> routeParam = new Pair<>("monster_name", encodedMonsterName);

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
