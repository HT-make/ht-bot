package com.htmake.htbot.discord.commands.dungeon.event.fieldDungeon;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.selections.SelectOption;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FieldDungeonMainButtonEvent {

    public void execute(ButtonInteractionEvent event) {
        MessageEmbed embed = buildEmbed(event.getUser());
        StringSelectMenu menu = buildMenu();

        List<ActionRow> actionRowList = new ArrayList<>();
        actionRowList.add(ActionRow.of(menu));
        actionRowList.add(ActionRow.of(Button.danger("cancel", "닫기")));

        event.getHook().editOriginalEmbeds(embed)
                .setComponents(actionRowList)
                .queue();
    }

    private MessageEmbed buildEmbed(User user) {
        String profileUrl = user.getAvatarUrl() != null ? user.getAvatarUrl() : user.getDefaultAvatarUrl();

        return new EmbedBuilder()
                .setColor(Color.GREEN)
                .setAuthor(user.getName(), null, profileUrl)
                .setTitle("필드 던전")
                .setDescription(
                        """
                        모든 필드는 자유롭게 입장이 가능합니다.
                        
                        필드 던전은 총 10개의 스테이지로 진행됩니다.
                                              
                        전투에서 패배 시 해당 던전에서 얻은
                        아이템을 모두 잃으니 주의하세요!
                        (전투 중 후퇴 시에도 패배로 처리됩니다.)
                        """
                )
                .build();
    }

    private StringSelectMenu buildMenu() {
        return StringSelectMenu.create("dungeonMenu")
                .setPlaceholder("던전 선택")
                .addOptions(Arrays.asList(
                        SelectOption.of("끝없는 평야 | 권장 레벨 1~10", "dungeon-field-F01"),
                        SelectOption.of("요정의 숲 | 권장 레벨 10~20", "dungeon-field-F02"),
                        SelectOption.of("끈적이는 늪 | 권장 레벨 20~30", "dungeon-field-F03"),
                        SelectOption.of("스산한 언덕 | 권장 레벨 30~40", "dungeon-field-F04"),
                        SelectOption.of("몰락한 왕국 | 권장 레벨 40~50", "dungeon-field-F05"),
                        SelectOption.of("거인들의 땅 | 권장 레벨 50~60", "dungeon-field-F06"),
                        SelectOption.of("눈먼자들의 지하도시 | 권장 레벨 60~70", "dungeon-field-F07"),
                        SelectOption.of("추락한 부유섬 | 권장 레벨 70~80", "dungeon-field-F08"),
                        SelectOption.of("드래곤 협곡 | 권장 레벨 80~90", "dungeon-field-F09"),
                        SelectOption.of("차원의 끝자락 | 권장 레벨 90~100", "dungeon-field-F10")
                ))
                .build();
    }
}
