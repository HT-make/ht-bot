package com.htmake.htbot.discord.commands.dungeon.util;

import com.htmake.htbot.cache.Caches;
import com.htmake.htbot.discord.commands.battle.cache.MonsterStatusCache;
import com.htmake.htbot.discord.commands.battle.cache.PlayerStatusCache;
import com.htmake.htbot.discord.commands.battle.cache.SituationCache;
import com.htmake.htbot.discord.commands.battle.data.MonsterStatus;
import com.htmake.htbot.discord.commands.battle.data.PlayerStatus;
import com.htmake.htbot.discord.commands.battle.data.Situation;
import com.htmake.htbot.domain.dungeon.entity.Monster;
import com.htmake.htbot.unirest.HttpClient;
import com.htmake.htbot.unirest.impl.HttpClientImpl;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import kotlin.Pair;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.json.JSONArray;
import org.json.JSONObject;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class DungeonUtil {

    private final HttpClient httpClient;

    private final PlayerStatusCache playerStatusCache;
    private final MonsterStatusCache monsterStatusCache;
    private final SituationCache situationCache;

    public DungeonUtil() {
        this.httpClient = new HttpClientImpl();

        this.playerStatusCache = Caches.playerStatusCache;
        this.monsterStatusCache = Caches.monsterStatusCache;
        this.situationCache = Caches.situationCache;
    }

    public MessageEmbed buildEmbed(String dungeonName, Monster monster, JSONObject playerObject) {

        return new EmbedBuilder()
                .setColor(Color.GREEN)
                .setTitle(dungeonName)
                .setDescription("Lv." + monster.getLevel() + " " + monster.getName())

                .addField(":crossed_swords: 공격력", "" + monster.getDamage(), true)
                .addField(":heart: 체력", "" + monster.getHealth(), true)
                .addField(":shield: 방어력", "" + monster.getDefence(), true)

                .addField(":video_game: 전투 현황", monster.getName() +"이/가 나타났다!", false)
                .addBlankField(false)

                .addField(":crossed_swords: 공격력", "" + playerObject.getInt("damage"), true)
                .addField(":heart: 체력", "" + playerObject.getInt("health"), true)
                .addField(":shield: 방어력", "" + playerObject.getInt("defence"), true)

                .addField(":large_blue_diamond: 마나", "" + playerObject.getInt("mana"), true)
                .addField(":boom: 치명타 확률", playerObject.getInt("criticalChance") + "%", true)
                .addField(":boom: 치명타 데미지", playerObject.getInt("criticalDamage") + "%", true)

                .setFooter("Lv." + playerObject.getInt("level") + " " + playerObject.get("name"))
                .build();
    }

    public HttpResponse<JsonNode> dungeonResponse(String dungeonId) {
        String endPoint = "/dungeon/{dungeon_id}";
        Pair<String, String> routeParam = new Pair<>("dungeon_id", dungeonId);

        return httpClient.sendGetRequest(endPoint, routeParam);
    }

    public ArrayList<Monster> toMonsterList(JSONArray monsterArray) {
        ArrayList<Monster> monsterList = new ArrayList<>();

        for (int i = 0; i < monsterArray.length(); i++) {
            JSONObject monsterObject = monsterArray.getJSONObject(i);

            Monster monster = Monster.builder()
                    .id(monsterObject.getString("id"))
                    .name(monsterObject.getString("name"))
                    .level(monsterObject.getInt("level"))
                    .damage(monsterObject.getInt("damage"))
                    .health(monsterObject.getInt("health"))
                    .defence(monsterObject.getInt("defence"))
                    .exp(monsterObject.getInt("exp"))
                    .gold(monsterObject.getInt("gold"))
                    .build();

            monsterList.add(monster);
        }

        monsterList.sort(Comparator.comparingInt(Monster::getLevel));

        return monsterList;
    }

    public JSONObject playerDataResponse(String playerId) {
        String endPoint = "/player/battle/{player_id}";
        Pair<String, String> routeParam = new Pair<>("player_id", playerId);

        HttpResponse<JsonNode> response = httpClient.sendGetRequest(endPoint, routeParam);

        if (response.getStatus() == 200) {
            return response.getBody().getObject();
        }

        return null;
    }

    public Monster randomMonster(ArrayList<Monster> monsterList, int stage) {
        Random random = new Random();

        int min = (stage % 2 == 0 ? stage / 2 : stage / 2 + 1) - 1;
        int max = (stage % 2 == 0 ? min + 3 : min + 2) + 1;

        int ran = random.nextInt(min, max);

        return monsterList.get(ran);
    }

    public void saveMonsterStatus(String playerId, Monster monster) {
        MonsterStatus monsterStatus = MonsterStatus.builder()
                .id(monster.getId())
                .name(monster.getName())
                .damage(monster.getDamage())
                .health(monster.getHealth())
                .defence(monster.getDefence())
                .build();

        monsterStatusCache.put(playerId, monsterStatus);
    }

    public void savePlayerStatus(String playerId, JSONObject playerObject) {
        PlayerStatus playerStatus = PlayerStatus.builder()
                .damage(playerObject.getInt("damage"))
                .health(playerObject.getInt("health"))
                .defence(playerObject.getInt("defence"))
                .mana(playerObject.getInt("mana"))
                .criticalChance(playerObject.getInt("criticalChance"))
                .criticalDamage(playerObject.getInt("criticalDamage"))
                .build();

        playerStatusCache.put(playerId, playerStatus);
    }

    public void saveSituation(String playerId, MessageEmbed.Field field) {
        List<String> messageList = new ArrayList<>();
        messageList.add(field.getValue());

        Situation situation = Situation.builder()
                .messageList(messageList)
                .build();

        situationCache.put(playerId, situation);
    }
}
