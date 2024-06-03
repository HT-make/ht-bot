package com.htmake.htbot.discord.commands.shop;

import com.htmake.htbot.discord.commands.shop.event.*;
import com.htmake.htbot.discord.util.ErrorUtil;
import com.htmake.htbot.discord.util.MessageUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class ShopCommand extends ListenerAdapter {

    private final RandomShopButtonEvent randomShopButtonEvent;

    private final BossShopButtonEvent bossShopButtonEvent;

    private final RandomShopPurchaseSelectEvent randomShopPurchaseSelectEvent;

    private final BossShopPurchaseSelectEvent bossShopPurchaseSelectEvent;

    private final BossShopPageButtonEvent bossShopPageButtonEvent;

    private final ItemSellSlashEvent itemSellSlashEvent;

    private final ShopSlashEvent shopSlashEvent;

    private final ErrorUtil errorUtil;
    private final MessageUtil messageUtil;

    public ShopCommand() {
        this.randomShopButtonEvent = new RandomShopButtonEvent();
        this.bossShopButtonEvent = new BossShopButtonEvent();
        this.bossShopPageButtonEvent = new BossShopPageButtonEvent();

        this.randomShopPurchaseSelectEvent = new RandomShopPurchaseSelectEvent();
        this.bossShopPurchaseSelectEvent = new BossShopPurchaseSelectEvent();

        this.itemSellSlashEvent = new ItemSellSlashEvent();
        this.shopSlashEvent = new ShopSlashEvent();

        this.errorUtil = new ErrorUtil();
        this.messageUtil = new MessageUtil();
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        String command = event.getName();

        if (command.equals("상점")) {
            if (messageUtil.validCheck(event.getUser().getId())) {
                errorUtil.sendError(event, "작업 실패", "현재 다른 작업을 진행중입니다.");
                return;
            }

            shopSlashEvent.execute(event);
        } else if (command.equals("상점-판매")){
            if (messageUtil.validCheck(event.getUser().getId())) {
                errorUtil.sendError(event, "작업 실패", "현재 다른 작업을 진행중입니다.");
                return;
            }

            itemSellSlashEvent.execute(event);
        }
    }

    @Override
    public void onStringSelectInteraction(StringSelectInteractionEvent event) {
        List<String> componentList = List.of(event.getValues().get(0).split("-"));

        if (componentList.get(0).equals("shop")) {
            if (messageUtil.validCheck(event.getMessage(), event.getUser().getName())) {
                errorUtil.sendError(event.getHook(), "제한된 버튼", "이 버튼은 이용할 수 없습니다.");
                return;
            }

            if (componentList.get(1).equals("purchase")) {
                switch (componentList.get(2)) {
                    case "random" -> randomShopPurchaseSelectEvent.execute(event, componentList.get(3));
                    case "boss" -> bossShopPurchaseSelectEvent.execute(event, componentList.get(3));
                }
            }
        }
    }

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        List<String> componentList = List.of(event.getComponentId().split("-"));

        if (componentList.get(0).equals("shop")) {
            if (messageUtil.validCheck(event.getMessage(), event.getUser().getName())) {
                errorUtil.sendError(event.getHook(), "제한된 버튼", "이 버튼은 이용할 수 없습니다.");
                return;
            }

            if (componentList.get(1).equals("random")) {
                randomShopButtonEvent.execute(event);
            }

            if (componentList.get(1).equals("boss")) {
                bossShopButtonEvent.execute(event);
            }

            if (componentList.get(1).equals("page")){
                if (componentList.get(2).equals("boss")) {
                    bossShopPageButtonEvent.execute(event, Integer.parseInt(componentList.get(3)));
                }
            }
        }
    }
}
