package com.htmake.htbot.discord.util;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.awt.*;
import java.util.Collections;

public class ErrorUtil {

    public void sendError(SlashCommandInteractionEvent event, String title, String description) {
        MessageEmbed embed = buildEmbed(title, description);
        event.replyEmbeds(embed).queue();
    }

    public void sendError(Message message, String title, String description) {
        MessageEmbed embed = buildEmbed(title, description);
        message.editMessageComponents(Collections.emptyList()).queue();
        message.editMessageEmbeds(embed).queue();
    }

    private MessageEmbed buildEmbed(String title, String description) {
        return new EmbedBuilder()
                .setColor(Color.ORANGE)
                .setTitle(":warning: " + title)
                .setDescription(description)
                .build();
    }
}
