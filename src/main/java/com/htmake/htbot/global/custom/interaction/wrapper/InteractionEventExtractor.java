package com.htmake.htbot.global.custom.interaction.wrapper;

import com.htmake.htbot.global.custom.interaction.enums.EventType;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

public abstract class InteractionEventExtractor<T, R> implements InteractionEventWrapper {

    protected final T event;
    protected final EventType eventType;
    protected R reply;

    protected InteractionEventExtractor(T event, EventType eventType) {
        this.event = event;
        this.eventType = eventType;
        this.reply = null;
    }

    @Override
    public SlashCommandInteractionEvent getSlashEvent() {
        return null;
    }

    @Override
    public ButtonInteractionEvent getButtonEvent() {
        return null;
    }

    @Override
    public EventType getEventType() {
        return eventType;
    }

    protected abstract R getReplyInstance(MessageEmbed embed);

    @Override
    public InteractionEventWrapper replyEmbed(MessageEmbed embed) {
        this.reply = getReplyInstance(embed);
        return this;
    }
}
