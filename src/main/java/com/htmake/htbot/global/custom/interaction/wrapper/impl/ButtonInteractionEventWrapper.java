package com.htmake.htbot.global.custom.interaction.wrapper.impl;

import com.htmake.htbot.global.custom.interaction.enums.EventType;
import com.htmake.htbot.global.custom.interaction.wrapper.InteractionEventExtractor;
import com.htmake.htbot.global.custom.interaction.wrapper.InteractionEventWrapper;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.ItemComponent;
import net.dv8tion.jda.api.interactions.components.LayoutComponent;
import net.dv8tion.jda.api.requests.restaction.WebhookMessageEditAction;

import java.util.Collection;

public class ButtonInteractionEventWrapper extends InteractionEventExtractor<ButtonInteractionEvent, WebhookMessageEditAction<Message>> {

    public ButtonInteractionEventWrapper(ButtonInteractionEvent event) {
        super(event, EventType.BUTTON);
    }

    @Override
    protected WebhookMessageEditAction<Message> getReplyInstance(MessageEmbed embed) {
        return event.getHook().editOriginalEmbeds(embed);
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
    public ButtonInteractionEvent getButtonEvent() {
        return event;
    }
}
