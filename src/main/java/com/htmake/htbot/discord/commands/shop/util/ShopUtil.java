package com.htmake.htbot.discord.commands.shop.util;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.awt.*;

public class ShopUtil {

    public void successPurchase(SlashCommandInteractionEvent event, int gold) {
        MessageEmbed embed = new EmbedBuilder()
                .setColor(Color.GREEN)
                .setTitle("아이템 구매에 성공했습니다!")
                .setDescription("보유 잔액: " + gold)
                .build();

        event.replyEmbeds(embed).queue();
    }

    public void errorMessage(SlashCommandInteractionEvent event, String title, String message) {
        MessageEmbed embed = new EmbedBuilder()
                .setColor(Color.ORANGE)
                .setTitle(":warning: " + title)
                .setDescription(message)
                .build();

        event.replyEmbeds(embed).queue();
    }
}
