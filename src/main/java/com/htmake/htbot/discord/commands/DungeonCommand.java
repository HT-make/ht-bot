package com.htmake.htbot.discord.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.selections.SelectOption;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.util.Arrays;

@Component
public class DungeonCommand extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        String command = event.getName();

        if (command.equals("던전-입장")) {
            handleDungeonInfo(event);
        }
    }

    private void handleDungeonInfo(SlashCommandInteractionEvent event) {

        MessageEmbed embed = new EmbedBuilder()
                .setColor(Color.GREEN)
                .setAuthor(event.getUser().getName())
                .setTitle(":key:던전")
                .setDescription(
                        """
                        :warning: 주의 :warning:
                        전투에서 패배 시 해당 던전에서 얻은
                        아이템을 모두 잃으니 주의하세요!
                        """)
                .build();

        StringSelectMenu menu = StringSelectMenu.create("dungeonMenu")
                .setPlaceholder("던전 선택")
                .addOptions(Arrays.asList(
                        SelectOption.of("드넓은 초원 | 권장 레벨 1~10", "dungeon1"),
                        SelectOption.of("깊은 동굴 | 권장 레벨 10~20", "dungeon2"),
                        SelectOption.of("끈적이는 늪 | 권장 레벨 20~30", "dungeon3"),
                        SelectOption.of("어두운 숲 | 권장 레벨 30~40", "dungeon4"),
                        SelectOption.of("몰락한 성 | 권장 레벨 40~50", "dungeon5"),
                        SelectOption.of("용암 지대 | 권장 레벨 50~60", "dungeon6")
                ))
                .build();

        event.replyEmbeds(embed)
                .addActionRow(menu)
                .addActionRow(Button.danger("cancel", "닫기"))
                .queue();
    }
}
