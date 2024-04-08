package com.htmake.htbot.discord.commands.battle;

import com.htmake.htbot.discord.commands.battle.event.BattleAttackEvent;
import com.htmake.htbot.discord.commands.battle.event.BattlePotionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BattleCommand extends ListenerAdapter {

    private final BattleAttackEvent battleAttackEvent;

    public BattleCommand() {
        battleAttackEvent = new BattleAttackEvent();
    }

    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {

        List<String> components = List.of(event.getComponentId().split("-"));
        String component = components.get(0);

        if (component.equals("attack")) {
            battleAttackEvent.execute(event);
        } else if (component.equals("potion")) {
            new BattlePotionEvent(event, components.get(1));
        }
    }
}