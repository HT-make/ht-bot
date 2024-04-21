package com.htmake.htbot.discord.commands.battle.util;

import com.htmake.htbot.global.cache.Caches;
import com.htmake.htbot.discord.commands.battle.cache.MonsterStatusCache;
import com.htmake.htbot.discord.commands.battle.cache.PlayerStatusCache;
import com.htmake.htbot.discord.commands.battle.cache.SituationCache;
import com.htmake.htbot.discord.commands.battle.data.MonsterStatus;
import com.htmake.htbot.discord.commands.battle.data.PlayerStatus;
import com.htmake.htbot.discord.commands.battle.data.Situation;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

import java.awt.*;
import java.util.List;

public class BattleUtil {

    private final PlayerStatusCache playerStatusCache;
    private final MonsterStatusCache monsterStatusCache;
    private final SituationCache situationCache;

    public BattleUtil() {
        this.playerStatusCache = Caches.playerStatusCache;
        this.monsterStatusCache = Caches.monsterStatusCache;
        this.situationCache = Caches.situationCache;
    }

    public void updateSituation(String playerId, String message) {
        Situation situation = situationCache.get(playerId);
        List<String> messageList = situation.getMessageList();

        if (messageList.size() >= 5) {
            messageList.remove(0);
        }
        messageList.add(message);

        situation.setMessageList(messageList);
        situationCache.put(playerId, situation);
    }

    public void editEmbed(ButtonInteractionEvent event, PlayerStatus playerStatus, MonsterStatus monsterStatus) {
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
                .addField(":shield: 방어력", "" + playerStatus.getDefence(), true)

                .addField(":large_blue_diamond: 마나", "" + playerStatus.getMana(), true)
                .addField(":boom: 치명타 확률", playerStatus.getCriticalChance() + "%", true)
                .addField(":boom: 치명타 데미지", playerStatus.getCriticalDamage() + "%", true)

                .setFooter("" + embed.getFooter().getText())
                .build();

        event.getMessage().editMessageEmbeds(newEmbed).queue();
    }

    public void removeCurrentBattleCache(String playerId) {
        playerStatusCache.remove(playerId);
        monsterStatusCache.remove(playerId);
        situationCache.remove(playerId);
    }
}
