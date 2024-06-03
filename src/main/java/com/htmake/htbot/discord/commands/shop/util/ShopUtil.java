package com.htmake.htbot.discord.commands.shop.util;

import com.htmake.htbot.discord.commands.shop.data.BossShopItem;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;

import java.awt.*;
import java.util.Collections;
import java.util.List;

public class ShopUtil {

    public void successPurchase(StringSelectInteractionEvent event, int gold) {
        MessageEmbed embed = new EmbedBuilder()
                .setColor(Color.GREEN)
                .setTitle("아이템 구매에 성공했습니다!")
                .setDescription("보유 잔액: " + gold)
                .build();

        event.getHook().editOriginalComponents(Collections.emptyList()).queue();
        event.getHook().editOriginalEmbeds(embed).queue();
    }

    public String format(List<BossShopItem> itemList) {
        StringBuilder sb = new StringBuilder();

        for (BossShopItem item : itemList) {
            sb.append("- ")
                    .append(item.getName())
                    .append(" - ")
                    .append(item.getCoin())
                    .append("코인\n");
        }

        return sb.toString();
    }
}
