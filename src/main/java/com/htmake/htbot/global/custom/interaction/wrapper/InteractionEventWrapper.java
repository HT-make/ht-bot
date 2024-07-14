package com.htmake.htbot.global.custom.interaction.wrapper;

import com.htmake.htbot.global.custom.interaction.enums.EventType;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.ItemComponent;
import net.dv8tion.jda.api.interactions.components.LayoutComponent;

import java.util.Collection;

public interface InteractionEventWrapper {

    InteractionEventWrapper replyEmbed(MessageEmbed embed);

    InteractionEventWrapper setComponents(Collection<? extends LayoutComponent> components);

    InteractionEventWrapper setActionRow(ItemComponent... itemComponents);

    void queue();

    EventType getEventType();

    SlashCommandInteractionEvent getSlashEvent();

    ButtonInteractionEvent getButtonEvent();
}
