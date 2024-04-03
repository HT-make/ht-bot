package com.htmake.htbot.discord.commands.battle;

import com.htmake.htbot.discord.commands.battle.event.BattleAttackEvent;
import com.htmake.htbot.discord.commands.battle.event.BattlePotionEvent;
import com.htmake.htbot.unirest.HttpClient;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class BattleCommand extends ListenerAdapter {

    private final HttpClient httpClient;

    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
        List<String> components = List.of(event.getComponentId().split("-"));
        String component = components.get(0);

        if (component.equals("attack")) {
            new BattleAttackEvent(event, httpClient);
        } else if (component.equals("potion")) {
            new BattlePotionEvent(event, components.get(1));
        }
    }
}