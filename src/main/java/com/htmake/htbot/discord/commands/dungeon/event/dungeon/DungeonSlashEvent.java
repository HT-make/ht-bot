package com.htmake.htbot.discord.commands.dungeon.event.dungeon;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.selections.SelectOption;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;

import java.awt.*;
import java.util.Arrays;

public class DungeonSlashEvent {

    public void execute(SlashCommandInteractionEvent event) {
        MessageEmbed embed = buildEmbed(event.getUser());
        StringSelectMenu menu = buildMenu();

        event.replyEmbeds(embed)
                .addActionRow(menu)
                .addActionRow(Button.danger("cancel", "닫기"))
                .queue();
    }

    private MessageEmbed buildEmbed(User user) {
        String profileUrl = user.getAvatarUrl() != null ? user.getAvatarUrl() : user.getDefaultAvatarUrl();

        return new EmbedBuilder()
                .setColor(Color.GREEN)
                .setAuthor(user.getName(), null, profileUrl)
                .setTitle("던전 선택")
                .addField("필드 던전", "평범한 사냥터 입니다.", false)
                .addField("보스 던전", "강력한 보스 몬스터가 존재하는 곳입니다.", false)
                .build();
    }

    private StringSelectMenu buildMenu() {
        return StringSelectMenu.create("dungeonMenu")
                .setPlaceholder("던전 선택")
                .addOptions(Arrays.asList(
                        SelectOption.of("필드 던전", "dungeon-enter-field"),
                        SelectOption.of("보스 던전", "dungeon-enter-boss")
                ))
                .build();
    }
}
