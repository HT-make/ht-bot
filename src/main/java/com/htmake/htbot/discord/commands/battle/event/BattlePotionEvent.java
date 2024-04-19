package com.htmake.htbot.discord.commands.battle.event;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

public class BattlePotionEvent {

    public BattlePotionEvent(ButtonInteractionEvent event, String component) {
        if (component.equals("open")) {
            openPotion(event);
        } else if (component.equals("close")) {
            closePotion(event);
        }
    }

    private void openPotion(ButtonInteractionEvent event) {
        Message message = event.getMessage();
        MessageEmbed embed = message.getEmbeds().get(0);

        message.editMessageEmbeds(embed)
                .setActionRow(
                        Button.primary("battle-potion-healing", "체력 포션"),
                        Button.primary("battle-potion-mana", "마나 포션"),
                        Button.danger("battle-potion-close", "닫기")
                ).queue();
    }

    private void closePotion(ButtonInteractionEvent event) {
        Message message = event.getMessage();
        MessageEmbed embed = message.getEmbeds().get(0);

        message.editMessageEmbeds(embed)
                .setActionRow(
                        Button.success("battle-attack", "공격"),
                        Button.primary("battle-potion-open", "포션"),
                        Button.danger("battle-retreat", "후퇴")
                )
                .queue();
    }
}
