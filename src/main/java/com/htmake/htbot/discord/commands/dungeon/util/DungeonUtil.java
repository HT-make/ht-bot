package com.htmake.htbot.discord.commands.dungeon.util;

import com.htmake.htbot.discord.commands.dungeon.data.DungeonMonster;
import com.htmake.htbot.discord.commands.dungeon.data.DungeonPlayer;
import com.htmake.htbot.global.cache.CacheFactory;
import com.htmake.htbot.discord.commands.battle.cache.MonsterStatusCache;
import com.htmake.htbot.discord.commands.battle.cache.PlayerStatusCache;
import com.htmake.htbot.discord.commands.battle.cache.SituationCache;
import com.htmake.htbot.discord.commands.battle.data.MonsterStatus;
import com.htmake.htbot.discord.commands.battle.data.PlayerStatus;
import com.htmake.htbot.discord.commands.battle.data.Situation;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class DungeonUtil {

    private final PlayerStatusCache playerStatusCache;
    private final MonsterStatusCache monsterStatusCache;
    private final SituationCache situationCache;

    public DungeonUtil() {
        this.playerStatusCache = CacheFactory.playerStatusCache;
        this.monsterStatusCache = CacheFactory.monsterStatusCache;
        this.situationCache = CacheFactory.situationCache;
    }

    public MessageEmbed buildEmbed(String dungeonTitle, DungeonMonster dungeonMonster, DungeonPlayer dungeonPlayer, User user) {

        StringBuilder sb = new StringBuilder();

        sb.append("```| ").append(dungeonMonster.getName()).append("이/가 나타났다!\n");
        sb.append("|\n".repeat(6));
        sb.append("```");

        String profileUrl = user.getAvatarUrl() != null ? user.getAvatarUrl() : user.getDefaultAvatarUrl();

        return new EmbedBuilder()
                .setColor(Color.GREEN)
                .setAuthor(user.getName(), null, profileUrl)
                .setTitle(dungeonTitle)
                .setDescription("Lv." + dungeonMonster.getLevel() + " " + dungeonMonster.getName())

                .addField(":crossed_swords: 공격력", "" + dungeonMonster.getDamage(), true)
                .addField(":heart: 체력", "" + dungeonMonster.getHealth(), true)
                .addField(":shield: 방어력", "" + dungeonMonster.getDefence(), true)

                .addField(":video_game: 전투 현황", sb.toString(), false)
                .addBlankField(false)

                .addField(":crossed_swords: 공격력", "" + dungeonPlayer.getDamage(), true)
                .addField(":heart: 체력", "" + dungeonPlayer.getHealth(), true)
                .addField(":shield: 방어력", "" + dungeonPlayer.getDefence(), true)
                .addField(":large_blue_diamond: 마나", "" + dungeonPlayer.getMana(), true)
                .addField(":boom: 치명타 확률",  dungeonPlayer.getCriticalChance()+ "%", true)
                .addField(":boom: 치명타 데미지", dungeonPlayer.getCriticalDamage() + "%", true)

                .setFooter("Lv." + dungeonPlayer.getLevel() + " " + user.getName())
                .build();
    }

    public void saveMonsterStatus(String playerId, DungeonMonster dungeonMonster) {
        MonsterStatus monsterStatus = MonsterStatus.builder()
                .id(dungeonMonster.getId())
                .name(dungeonMonster.getName())
                .damage(dungeonMonster.getDamage())
                .health(dungeonMonster.getHealth())
                .defence(dungeonMonster.getDefence())
                .skillName(dungeonMonster.getSkillName())
                .skillDamage(dungeonMonster.getSkillDamage())
                .build();

        monsterStatusCache.put(playerId, monsterStatus);
    }

    public void savePlayerStatus(String playerId, DungeonPlayer dungeonPlayer) {
        PlayerStatus playerStatus = PlayerStatus.builder()
                .damage(dungeonPlayer.getDamage())
                .health(dungeonPlayer.getHealth())
                .defence(dungeonPlayer.getDefence())
                .mana(dungeonPlayer.getMana())
                .criticalChance(dungeonPlayer.getCriticalChance())
                .criticalDamage(dungeonPlayer.getCriticalDamage())
                .playerSkill(dungeonPlayer.getPlayerSkill())
                .build();

        playerStatusCache.put(playerId, playerStatus);
    }

    public void saveSituation(String playerId, String monsterName) {
        List<String> messageList = new ArrayList<>();
        messageList.add(monsterName + "이/가 나타났다!");

        Situation situation = Situation.builder()
                .messageList(messageList)
                .build();

        situationCache.put(playerId, situation);
    }
}
