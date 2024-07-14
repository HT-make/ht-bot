package com.htmake.htbot.global.custom.interaction.wrapper.impl;

import com.htmake.htbot.global.custom.interaction.enums.EventType;
import com.htmake.htbot.global.custom.interaction.wrapper.InteractionEventExtractor;
import com.htmake.htbot.global.custom.interaction.wrapper.InteractionEventWrapper;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.components.ItemComponent;
import net.dv8tion.jda.api.interactions.components.LayoutComponent;
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyCallbackAction;

import java.util.Collection;

public class SlashCommandInteractionEventWrapper extends InteractionEventExtractor<SlashCommandInteractionEvent, ReplyCallbackAction> {

    public SlashCommandInteractionEventWrapper(SlashCommandInteractionEvent event) {
        super(event, EventType.SLASH);
    }

    @Override
    protected ReplyCallbackAction getReplyInstance(MessageEmbed embed) {
        return event.replyEmbeds(embed);
    }

    @Override
    public InteractionEventWrapper setComponents(Collection<? extends LayoutComponent> components) {
        reply.setComponents(components);
        return this;
    }

    @Override
    public InteractionEventWrapper setActionRow(ItemComponent... itemComponents) {
        reply.setActionRow(itemComponents);
        return this;
    }

    @Override
    public void queue() {
        reply.queue();
    }

    @Override
    public SlashCommandInteractionEvent getSlashEvent() {
        return event;
    }
}
