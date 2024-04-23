package com.htmake.htbot.discord.commands.inventory;

import com.htmake.htbot.discord.commands.inventory.event.InventoryButtonEvent;
import com.htmake.htbot.discord.commands.inventory.event.InventorySlashEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class InventoryCommand extends ListenerAdapter {

    private final InventorySlashEvent inventorySlashEvent;
    private final InventoryButtonEvent inventoryButtonEvent;

    public InventoryCommand() {
        this.inventorySlashEvent = new InventorySlashEvent();
        this.inventoryButtonEvent = new InventoryButtonEvent();
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        String command = event.getName();

        if (command.equals("인벤토리")) {
            inventorySlashEvent.execute(event);
        }
    }

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        List<String> components = List.of(event.getComponentId().split("-"));
        String component = components.get(0);

        if (component.equals("inventory")) {
            String pageButton = components.get(1);
            int page = Integer.parseInt(components.get(2));

            if (pageButton.equals("left") && page > 1) {
                inventoryButtonEvent.execute(event, page - 1);
            } else if (pageButton.equals("right") && page < 6) {
                inventoryButtonEvent.execute(event, page);
            }
        }
    }
}
