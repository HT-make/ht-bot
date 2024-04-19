package com.htmake.htbot.discord.commands.skill;

import com.htmake.htbot.discord.commands.skill.event.AvailableSkillListEvent;
import com.htmake.htbot.discord.commands.skill.event.RegisterSkillButtonEvent;
import com.htmake.htbot.discord.commands.skill.event.RegisterSkillSelectEvent;
import com.htmake.htbot.discord.commands.skill.event.RegisterSkillSlashEvent;
import kotlin.Pair;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SkillCommand extends ListenerAdapter {

    private final AvailableSkillListEvent availableSkillListEvent;

    private final RegisterSkillSlashEvent registerSkillSlashEvent;
    private final RegisterSkillButtonEvent registerSkillButtonEvent;
    private final RegisterSkillSelectEvent registerSkillSelectEvent;

    public SkillCommand() {
        this.availableSkillListEvent = new AvailableSkillListEvent();

        this.registerSkillSlashEvent = new RegisterSkillSlashEvent();
        this.registerSkillButtonEvent = new RegisterSkillButtonEvent();
        this.registerSkillSelectEvent = new RegisterSkillSelectEvent();
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        String command = event.getName();

        if (command.equals("스킬-목록")) {
            availableSkillListEvent.execute(event);
        } else if (command.equals("스킬-등록")) {
            registerSkillSlashEvent.execute(event);
        }
    }

    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
        List<String> componentList = List.of(event.getComponentId().split("-"));

        if (componentList.get(0).equals("skill")) {
            if (componentList.get(1).equals("register")) {
                registerSkillButtonEvent.execute(event, componentList.get(2));
            }
        }
    }

    @Override
    public void onStringSelectInteraction(@NotNull StringSelectInteractionEvent event) {
        List<String> componentList = List.of(event.getValues().get(0).split("-"));

        if (componentList.get(0).equals("skill")) {
            if (componentList.get(1).equals("register")) {
                Pair<String, String> value = new Pair<>(componentList.get(2), componentList.get(3));
                registerSkillSelectEvent.execute(event, value);
            }
        }
    }
}
