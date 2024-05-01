package com.htmake.htbot.discord.commands.dungeon.event;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.selections.SelectOption;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;

import java.awt.*;
import java.util.Arrays;

public class DungeonEntrySlashEvent {

    public void execute(SlashCommandInteractionEvent event) {
        MessageEmbed embed = buildEmbed(event.getUser());
        StringSelectMenu menu = buildMenu();

        event.replyEmbeds(embed)
                .addActionRow(menu)
                .addActionRow(Button.danger("cancel", "닫기"))
                .queue();
    }

    private MessageEmbed buildEmbed(User user) {
        String profileUrl = user.getAvatarUrl() != null ? user.getAvatarUrl() : user.getDefaultAvatarUrl();

        return new EmbedBuilder()
                .setColor(Color.GREEN)
                .setAuthor(user.getName(), null, profileUrl)
                .setTitle(":key: 던전")
                .setDescription(
                        """
                        :warning: 주의 :warning:
                        
                        전투에서 패배 시 해당 던전에서 얻은
                        아이템을 모두 잃으니 주의하세요!
                        
                        전투 중 후퇴 시에도 패배로 처리됩니다.
                        """
                )
                .build();
    }

    private StringSelectMenu buildMenu() {
        return StringSelectMenu.create("dungeonMenu")
                .setPlaceholder("던전 선택")
                .addOptions(Arrays.asList(
                        SelectOption.of("드넓은 초원 | 권장 레벨 1~10", "dungeon-enter-dungeon1"),
                        SelectOption.of("깊은 동굴 | 권장 레벨 10~20", "dungeon-enter-dungeon2"),
                        SelectOption.of("끈적이는 늪 | 권장 레벨 20~30", "dungeon-enter-dungeon3"),
                        SelectOption.of("어두운 숲 | 권장 레벨 30~40", "dungeon-enter-dungeon4"),
                        SelectOption.of("몰락한 성 | 권장 레벨 40~50", "dungeon-enter-dungeon5"),
                        SelectOption.of("용암 지대 | 권장 레벨 50~60", "dungeon-enter-dungeon6")
                ))
                .build();
    }
}
