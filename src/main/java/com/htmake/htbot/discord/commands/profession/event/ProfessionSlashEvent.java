package com.htmake.htbot.discord.commands.profession.event;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.awt.*;


public class ProfessionSlashEvent {

    public void execute(SlashCommandInteractionEvent event) {
        User user = event.getUser();

        MessageEmbed embed = buildEmbed(user);

        event.replyEmbeds(embed)
                .addActionRow(
                        Button.primary("promotion-class-first", "1차 전직"),
                        Button.primary("promotion-class-second", "2차 전직"),
                        Button.danger("cancel", "닫기")
                )
                .queue();
    }
    private MessageEmbed buildEmbed(User user) {
        String profileUrl = user.getAvatarUrl() != null ? user.getAvatarUrl() : user.getDefaultAvatarUrl();

        return new EmbedBuilder()
                .setColor(Color.GREEN)
                .setAuthor(user.getName(), null, profileUrl)
                .setTitle("전직")
                .addField(":star: 전직", "전직직", true)
                .build();
    }
}
