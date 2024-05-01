package com.htmake.htbot.discord.commands.shop;

import com.htmake.htbot.discord.commands.shop.event.RandomShopEvent;
import com.htmake.htbot.discord.commands.shop.event.RandomShopPurchaseEvent;
import com.htmake.htbot.discord.util.ErrorUtil;
import com.htmake.htbot.discord.util.MessageUtil;
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

    private final RandomShopPurchaseEvent randomShopPurchaseEvent;

    private final ErrorUtil errorUtil;
    private final MessageUtil messageUtil;

    public ShopCommand() {
        this.randomShopEvent = new RandomShopEvent();

        this.randomShopPurchaseEvent = new RandomShopPurchaseEvent();

        this.errorUtil = new ErrorUtil();
        this.messageUtil = new MessageUtil();
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        String command = event.getName();

        if (command.equals("랜덤-상점")) {
            if (messageUtil.validCheck(event.getUser().getId())) {
                errorUtil.sendError(event, "작업 실패", "현재 다른 작업을 진행중입니다.");
                return;
            }

            randomShopEvent.execute(event);
        } else if (command.equals("랜덤-상점-구매")) {
            if (messageUtil.validCheck(event.getUser().getId())) {
                errorUtil.sendError(event, "작업 실패", "현재 다른 작업을 진행중입니다.");
                return;
            }

            randomShopPurchaseEvent.execute(event);
        }
    }
}
