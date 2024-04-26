package com.htmake.htbot.discord.commands.battle;

import com.htmake.htbot.discord.commands.battle.event.BattleSkillButtonEvent;
import com.htmake.htbot.discord.commands.battle.event.PlayerAttackButtonEvent;
import com.htmake.htbot.discord.commands.battle.event.BattlePotionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BattleCommand extends ListenerAdapter {

    private final PlayerAttackButtonEvent playerAttackButtonEvent;
    private final BattleSkillButtonEvent battleSkillButtonEvent;

    public BattleCommand() {
        this.playerAttackButtonEvent = new PlayerAttackButtonEvent();
        this.battleSkillButtonEvent = new BattleSkillButtonEvent();
    }

    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
        List<String> componentList = List.of(event.getComponentId().split("-"));

        if (componentList.get(0).equals("battle")) {
            switch (componentList.get(1)) {
                case "attack" -> playerAttackButtonEvent.execute(event);
                case "skill" -> battleSkillButtonEvent.execute(event, componentList.get(2));
                case "potion" -> new BattlePotionEvent(event, componentList.get(2));
            }
        }
    }
}