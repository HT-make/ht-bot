package com.htmake.htbot.discord.util;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;

import java.awt.*;

public class ErrorUtil {

    public void sendError(SlashCommandInteractionEvent event, String title, String description) {
        MessageEmbed embed = buildEmbed(title, description);
        event.replyEmbeds(embed).queue();
    }

    public void sendError(InteractionHook hook, String title, String description) {
        MessageEmbed embed = buildEmbed(title, description);
        hook.setEphemeral(true);
        hook.sendMessageEmbeds(embed).queue();
    }

    private MessageEmbed buildEmbed(String title, String description) {
        return new EmbedBuilder()
                .setColor(Color.ORANGE)
                .setTitle(":warning: " + title)
                .setDescription(description)
                .build();
    }
}
