package com.htmake.htbot.discord.commands.dungeon.util;

import com.htmake.htbot.discord.commands.battle.data.*;
import com.htmake.htbot.discord.commands.battle.data.status.extend.MonsterOriginalStatus;
import com.htmake.htbot.discord.commands.battle.data.status.extend.PlayerOriginalStatus;
import com.htmake.htbot.discord.commands.dungeon.cache.DungeonTypeCache;
import com.htmake.htbot.discord.commands.dungeon.data.DungeonMonster;
import com.htmake.htbot.discord.commands.dungeon.data.DungeonPlayer;
import com.htmake.htbot.discord.commands.dungeon.enums.DungeonType;
import com.htmake.htbot.discord.skillAction.BasicSkill;
import com.htmake.htbot.discord.skillAction.factory.SkillFactory;
import com.htmake.htbot.discord.skillAction.type.SkillType;
import com.htmake.htbot.discord.util.FormatUtil;
import com.htmake.htbot.domain.player.enums.Job;
import com.htmake.htbot.global.cache.CacheFactory;
import com.htmake.htbot.discord.commands.battle.cache.MonsterDataCache;
import com.htmake.htbot.discord.commands.battle.cache.PlayerDataCache;
import com.htmake.htbot.discord.commands.battle.cache.SituationCache;
import com.htmake.htbot.discord.commands.battle.data.status.extend.MonsterStatus;
import com.htmake.htbot.discord.commands.battle.data.status.extend.PlayerStatus;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import org.json.JSONArray;
import org.json.JSONObject;

import java.awt.*;
import java.util.*;
import java.util.List;

public class DungeonUtil {

    private final PlayerDataCache playerDataCache;
    private final MonsterDataCache monsterDataCache;
    private final SituationCache situationCache;
    private final DungeonTypeCache dungeonTypeCache;

    public DungeonUtil() {
        this.playerDataCache = CacheFactory.playerDataCache;
        this.monsterDataCache = CacheFactory.monsterDataCache;
        this.situationCache = CacheFactory.situationCache;
        this.dungeonTypeCache = CacheFactory.dungeonTypeCache;
    }

    public MessageEmbed buildEmbed(String dungeonTitle, DungeonMonster dungeonMonster, DungeonPlayer dungeonPlayer, User user) {

        String profileUrl = user.getAvatarUrl() != null ? user.getAvatarUrl() : user.getDefaultAvatarUrl();

        String monsterInfo = "Lv." + dungeonMonster.getLevel() + " " + dungeonMonster.getName();
        String playerInfo = "Lv." + dungeonPlayer.getLevel() + " " + dungeonPlayer.getName();

        StringBuilder situation = new StringBuilder();
        situation.append("```| ").append(dungeonMonster.getName()).append("이/가 나타났다!\n");
        situation.append("|\n".repeat(6));
        situation.append("```");

        return new EmbedBuilder()
                .setColor(Color.GREEN)
                .setAuthor(user.getName(), null, profileUrl)
                .setTitle(dungeonTitle)

                .addField(monsterInfo, "", false)
                .addField(":crossed_swords: 공격력", "" + FormatUtil.decimalFormat(dungeonMonster.getDamage()), true)
                .addField(":heart: 체력", "" + FormatUtil.decimalFormat(dungeonMonster.getHealth()), true)
                .addField(":shield: 방어력", "" + FormatUtil.decimalFormat(dungeonMonster.getDefence()), true)
                .addField(":boom: 치명타 확률",  dungeonMonster.getCriticalChance()+ "%", true)
                .addField(":boom: 치명타 데미지", FormatUtil.decimalFormat(dungeonMonster.getCriticalDamage()) + "%", true)
                .addBlankField(true)

                .addField(":video_game: 전투 현황", situation.toString(), false)

                .addField(":crossed_swords: 공격력", "" + FormatUtil.decimalFormat(dungeonPlayer.getDamage()), true)
                .addField(":heart: 체력", "" + FormatUtil.decimalFormat(dungeonPlayer.getHealth()), true)
                .addField(":shield: 방어력", "" + FormatUtil.decimalFormat(dungeonPlayer.getDefence()), true)
                .addField(":large_blue_diamond: 마나", "" + FormatUtil.decimalFormat(dungeonPlayer.getMana()), true)
                .addField(":boom: 치명타 확률",  dungeonPlayer.getCriticalChance()+ "%", true)
                .addField(":boom: 치명타 데미지", FormatUtil.decimalFormat(dungeonPlayer.getCriticalDamage()) + "%", true)
                .addField(playerInfo, "", false)
                .build();
    }

    public void saveMonsterStatus(String playerId, DungeonMonster dungeonMonster) {
        MonsterStatus monsterStatus = new MonsterStatus(
                dungeonMonster.getLevel(),
                dungeonMonster.getName(),
                dungeonMonster.getDamage(),
                dungeonMonster.getHealth(),
                dungeonMonster.getDefence(),
                dungeonMonster.getCriticalChance(),
                dungeonMonster.getCriticalDamage(),
                dungeonMonster.getId(),
                dungeonMonster.getSkillChance(),
                dungeonMonster.getSkillList()
        );

        MonsterOriginalStatus monsterOriginalStatus = new MonsterOriginalStatus(
                dungeonMonster.getLevel(),
                dungeonMonster.getName(),
                dungeonMonster.getDamage(),
                dungeonMonster.getHealth(),
                dungeonMonster.getDefence(),
                dungeonMonster.getCriticalChance(),
                dungeonMonster.getCriticalDamage()
        );

        MonsterData monsterData = MonsterData.builder()
                .monsterStatus(monsterStatus)
                .monsterOriginalStatus(monsterOriginalStatus)
                .build();

        monsterDataCache.put(playerId, monsterData);
    }

    public void savePlayerStatus(String playerId, DungeonPlayer dungeonPlayer) {
        PlayerStatus playerStatus = new PlayerStatus(
                dungeonPlayer.getLevel(),
                dungeonPlayer.getName(),
                dungeonPlayer.getDamage(),
                dungeonPlayer.getHealth(),
                dungeonPlayer.getDefence(),
                dungeonPlayer.getCriticalChance(),
                dungeonPlayer.getCriticalDamage(),
                dungeonPlayer.getMana(),
                dungeonPlayer.getJob(),
                dungeonPlayer.getPlayerSkill()
        );

        PlayerOriginalStatus playerOriginalStatus = new PlayerOriginalStatus(
                dungeonPlayer.getLevel(),
                dungeonPlayer.getName(),
                dungeonPlayer.getDamage(),
                dungeonPlayer.getHealth(),
                dungeonPlayer.getDefence(),
                dungeonPlayer.getCriticalChance(),
                dungeonPlayer.getCriticalDamage()
        );

        PlayerData playerData = PlayerData.builder()
                .playerStatus(playerStatus)
                .playerOriginalStatus(playerOriginalStatus)
                .build();

        playerDataCache.put(playerId, playerData);
    }

    public void saveSituation(String playerId, String monsterName) {
        List<String> messageList = new ArrayList<>();
        messageList.add(monsterName + "이/가 나타났다!");

        Situation situation = Situation.builder()
                .messageList(messageList)
                .build();

        situationCache.put(playerId, situation);
    }

    public DungeonPlayer toDungeonPlayer(String playerName, JSONObject playerObject) {
        JSONArray playerSkillArray = playerObject.getJSONArray("skillList");
        Map<Integer, PlayerSkillStatus> playerSkillMap = new HashMap<>();
        Map<String, BasicSkill> basicSkillMap = SkillFactory.skillMap;

        for (int i = 0;i < playerSkillArray.length(); i++) {
            JSONObject playerSkillObject = playerSkillArray.getJSONObject(i);

            String id = playerSkillObject.getString("id");
            Integer number = playerSkillObject.getInt("number");

            PlayerSkillStatus playerSkillStatus = PlayerSkillStatus.builder()
                    .name(playerSkillObject.getString("name"))
                    .basicSkill(basicSkillMap.get(id))
                    .build();

            playerSkillMap.put(number, playerSkillStatus);
        }

        Job job = Job.valueOf(playerObject.getString("job"));
        int mana = playerObject.getInt("mana");
        int passiveHealth = (job.equals(Job.BLACK_WIZARD) ? mana * 10 : 0);

        return DungeonPlayer.builder()
                .level(playerObject.getInt("level"))
                .name(playerName)
                .damage(playerObject.getInt("damage"))
                .health(playerObject.getInt("health") + passiveHealth)
                .defence(playerObject.getInt("defence"))
                .mana(mana)
                .criticalChance(playerObject.getInt("criticalChance"))
                .criticalDamage(playerObject.getInt("criticalDamage"))
                .job(job)
                .playerSkill(playerSkillMap)
                .build();
    }

    public void saveDungeonType(String playerId, DungeonType dungeonType) {
        dungeonTypeCache.put(playerId, dungeonType);
    }

    public void setMonsterSkillList(List<MonsterSkillData> monsterSkillDataList, JSONArray monsterSkillArray) {
        for (int i = 0; i < monsterSkillArray.length(); i++) {
            JSONObject monsterSkillObject = monsterSkillArray.getJSONObject(i);

            MonsterSkillData monsterSkillData = MonsterSkillData.builder()
                    .name(monsterSkillObject.getString("name"))
                    .damage(monsterSkillObject.getInt("damage"))
                    .effect(monsterSkillObject.getString("effect"))
                    .chance(monsterSkillObject.getInt("chance"))
                    .skillType(SkillType.valueOf(monsterSkillObject.getString("skillType")))
                    .build();

            monsterSkillDataList.add(monsterSkillData);
        }
    }
}
