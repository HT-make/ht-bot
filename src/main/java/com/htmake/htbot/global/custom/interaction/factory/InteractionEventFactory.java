package com.htmake.htbot.global.custom.interaction.factory;

import com.htmake.htbot.global.custom.interaction.wrapper.InteractionEventWrapper;
import com.htmake.htbot.global.custom.interaction.wrapper.impl.ButtonInteractionEventWrapper;
import com.htmake.htbot.global.custom.interaction.wrapper.impl.SlashCommandInteractionEventWrapper;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

public class InteractionEventFactory {

    public static InteractionEventWrapper createWrapper(ButtonInteractionEvent buttonEvent) {
        return new ButtonInteractionEventWrapper(buttonEvent);
    }

    public static InteractionEventWrapper createWrapper(SlashCommandInteractionEvent slashCommandEvent) {
        return new SlashCommandInteractionEventWrapper(slashCommandEvent);
    }
}
