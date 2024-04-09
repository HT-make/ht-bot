package com.htmake.htbot.discord.commands.shop;

import com.htmake.htbot.discord.commands.shop.event.RandomShopEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class ShopCommand extends ListenerAdapter {

    private final RandomShopEvent randomShopEvent;

    public ShopCommand() {
        this.randomShopEvent = new RandomShopEvent();
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        String command = event.getName();

        if (command.equals("랜덤-상점")) {
            randomShopEvent.execute(event);
        } else if (command.equals("판매")) {
            System.out.println("판매");
        }
    }
}
