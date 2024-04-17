package com.htmake.htbot.discord.commands.skill.util;

import com.htmake.htbot.domain.skill.presentation.data.response.AvailableSkillResponse;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import org.json.JSONArray;
import org.json.JSONObject;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SkillEventUtil {

    public java.util.List<AvailableSkillResponse> toSkillList(JSONArray skillArray) {
        List<AvailableSkillResponse> skillList = new ArrayList<>();

        for (int i = 0; i < skillArray.length(); i++) {
            JSONObject skillObject = skillArray.getJSONObject(i);

            AvailableSkillResponse skill = AvailableSkillResponse.builder()
                    .id(skillObject.getLong("id"))
                    .name(skillObject.getString("name"))
                    .value(skillObject.getInt("value"))
                    .mana(skillObject.getInt("mana"))
                    .skillType(skillObject.getString("skillType"))
                    .isRegistered(skillObject.getString("isRegistered"))
                    .build();

            skillList.add(skill);
        }

        return skillList;
    }

    public String format(AvailableSkillResponse skill) {
        String skillType = skill.getSkillType().equals("ATTACK") ? "데미지" : "치유량";
        return String.format("%s: %d%% 마나: %d", skillType, skill.getValue(), skill.getMana());
    }

    public void errorMessage(SlashCommandInteractionEvent event, String title, String message) {
        MessageEmbed embed = new EmbedBuilder()
                .setColor(Color.ORANGE)
                .setTitle(":warning: " + title)
                .setDescription(message)
                .build();

        event.replyEmbeds(embed).queue();
    }

    public void errorMessage(ButtonInteractionEvent event, String title, String message) {
        MessageEmbed embed = new EmbedBuilder()
                .setColor(Color.ORANGE)
                .setTitle(":warning: " + title)
                .setDescription(message)
                .build();

        event.getMessage().editMessageComponents(Collections.emptyList()).queue();
        event.getMessage().replyEmbeds(embed).queue();
    }

    public void errorMessage(StringSelectInteractionEvent event, String title, String message) {
        MessageEmbed embed = new EmbedBuilder()
                .setColor(Color.ORANGE)
                .setTitle(":warning: " + title)
                .setDescription(message)
                .build();

        event.getMessage().editMessageComponents(Collections.emptyList()).queue();
        event.getMessage().replyEmbeds(embed).queue();
    }
}
