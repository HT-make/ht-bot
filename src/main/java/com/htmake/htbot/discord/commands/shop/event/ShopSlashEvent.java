package com.htmake.htbot.discord.commands.shop.event;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.awt.*;

public class ShopSlashEvent {
    public void execute(SlashCommandInteractionEvent event) {
        User user = event.getUser();

        MessageEmbed embed = buildEmbed(user);

        event.replyEmbeds(embed)
                .addActionRow(
                        Button.primary("shop-random", "랜덤 상점"),
                        Button.primary("shop-boss", "보스 상점"),
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
                .addField(":game_die: 랜덤 상점", "매시간마다 랜덤으로 변경되는 장비를 구매할 수 있습니다.", true)
                .addField(":dragon: 보스 상점", "보스재화로 보다 더 좋은 아이템을 구매할 수 있습니다.", true)
                .build();
    }
}
