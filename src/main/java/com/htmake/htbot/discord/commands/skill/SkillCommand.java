package com.htmake.htbot.discord.commands.skill;

import com.htmake.htbot.discord.commands.skill.event.AvailableSkillListEvent;
import com.htmake.htbot.discord.commands.skill.event.RegisterSkillEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

@Component
public class SkillCommand extends ListenerAdapter {

    private final AvailableSkillListEvent availableSkillListEvent;
    private final RegisterSkillEvent registerSkillEvent;

    public SkillCommand() {
        this.availableSkillListEvent = new AvailableSkillListEvent();
        this.registerSkillEvent = new RegisterSkillEvent();
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        String command = event.getName();

        if (command.equals("스킬-목록")) {
            availableSkillListEvent.execute(event);
        } else if (command.equals("스킬-등록")) {
            registerSkillEvent.execute(event);
        }
    }
}
