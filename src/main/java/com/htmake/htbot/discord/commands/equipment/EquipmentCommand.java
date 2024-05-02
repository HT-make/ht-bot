package com.htmake.htbot.discord.commands.equipment;

import com.htmake.htbot.discord.commands.equipment.event.EquipmentEquipSlashEvent;
import com.htmake.htbot.discord.util.ErrorUtil;
import com.htmake.htbot.discord.util.MessageUtil;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class EquipmentCommand extends ListenerAdapter {

    private final EquipmentEquipSlashEvent equipmentEquipSlashEvent;

    private final ErrorUtil errorUtil;
    private final MessageUtil messageUtil;

    public EquipmentCommand() {
        this.equipmentEquipSlashEvent = new EquipmentEquipSlashEvent();

        this.errorUtil = new ErrorUtil();
        this.messageUtil = new MessageUtil();
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        String command = event.getName();

        if (command.equals("장비-장착")) {
            if (messageUtil.validCheck(event.getUser().getId())) {
                errorUtil.sendError(event, "작업 실패", "현재 다른 작업을 진행중입니다.");
                return;
            }

            equipmentEquipSlashEvent.execute(event);
        }
    }
}
