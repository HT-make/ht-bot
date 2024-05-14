package com.htmake.htbot.discord.commands.dungeon.event.dungeon;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.awt.*;

public class DungeonSlashEvent {

    public void execute(SlashCommandInteractionEvent event) {
        MessageEmbed embed = buildEmbed(event.getUser());

        event.replyEmbeds(embed)
                .addActionRow(
                        Button.primary("dungeon-field-main", "필드 던전"),
                        Button.primary("dungeon-boss-main", "보스 던전"),
                        Button.danger("cancel", "닫기")
                )
                .queue();
    }

    private MessageEmbed buildEmbed(User user) {
        String profileUrl = user.getAvatarUrl() != null ? user.getAvatarUrl() : user.getDefaultAvatarUrl();

        String fieldDescription =
                """
                자유롭게 던전에 들어가
                몬스터를 처치하여 레벨
                업을 하고 장비도 얻으며
                강해져 보세요.
                """;

        String bossDescription =
                """
                이곳은 강력한 보스 몬스
                터가 존재합니다. 보스를
                처치하여 더욱더 강력한
                장비를 얻어보세요.
                """;

        return new EmbedBuilder()
                .setColor(Color.GREEN)
                .setAuthor(user.getName(), null, profileUrl)
                .setTitle("던전 선택")
                .addField(":herb: 필드 던전", fieldDescription, true)
                .addField(":key2: 보스 던전", bossDescription, true)
                .build();
    }
}
