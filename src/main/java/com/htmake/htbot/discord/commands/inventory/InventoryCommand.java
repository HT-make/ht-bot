package com.htmake.htbot.discord.commands.inventory;

import com.htmake.htbot.discord.commands.inventory.event.InventoryButtonEvent;
import com.htmake.htbot.discord.commands.inventory.event.InventorySelectEvent;
import com.htmake.htbot.discord.commands.inventory.event.InventorySlashEvent;
import com.htmake.htbot.discord.util.ErrorUtil;
import com.htmake.htbot.discord.util.MessageUtil;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class InventoryCommand extends ListenerAdapter {

    private final InventorySlashEvent inventorySlashEvent;
    private final InventoryButtonEvent inventoryButtonEvent;
    private final InventorySelectEvent inventorySelectEvent;

    private final ErrorUtil errorUtil;
    private final MessageUtil messageUtil;

    public InventoryCommand() {
        this.inventorySlashEvent = new InventorySlashEvent();
        this.inventoryButtonEvent = new InventoryButtonEvent();
        this.inventorySelectEvent = new InventorySelectEvent();

        this.errorUtil = new ErrorUtil();
        this.messageUtil = new MessageUtil();
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        String command = event.getName();

        if (command.equals("인벤토리")) {
            if (messageUtil.validCheck(event.getUser().getId())) {
                errorUtil.sendError(event, "작업 실패", "현재 다른 작업을 진행중입니다.");
                return;
            }

            inventorySlashEvent.execute(event);
        }
    }

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        List<String> componentList = List.of(event.getComponentId().split("-"));

        if (componentList.get(0).equals("inventory")) {
            if (messageUtil.validCheck(event.getMessage(), event.getUser().getName())) {
                errorUtil.sendError(event.getHook(), "제한된 버튼", "이 버튼은 이용할 수 없습니다.");
                return;
            }

            int page = Integer.parseInt(componentList.get(1));
            inventoryButtonEvent.execute(event, page);
        }
    }

    @Override
    public void onStringSelectInteraction(StringSelectInteractionEvent event) {
        List<String> componentList = List.of(event.getValues().get(0).split("-"));

        if (componentList.get(0).equals("inventory")) {
            if (messageUtil.validCheck(event.getMessage(), event.getUser().getName())) {
                errorUtil.sendError(event.getHook(), "제한된 버튼", "이 버튼은 이용할 수 없습니다.");
                return;
            }

            if (componentList.get(1).equals("category")) {
                inventorySelectEvent.execute(event, componentList.get(2));
            }
        }
    }
}
