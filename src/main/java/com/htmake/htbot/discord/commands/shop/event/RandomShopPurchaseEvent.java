package com.htmake.htbot.discord.commands.shop.event;

import com.htmake.htbot.global.unirest.HttpClient;
import com.htmake.htbot.global.unirest.impl.HttpClientImpl;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

public class RandomShopPurchaseEvent {
    private final HttpClient httpClient;

    public RandomShopPurchaseEvent() {
        this.httpClient = new HttpClientImpl();
    }


    public void execute(SlashCommandInteractionEvent event) {
        OptionMapping equipmentNameOption = event.getOption("장비이름");
    }
}
