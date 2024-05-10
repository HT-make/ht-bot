package com.htmake.htbot.discord.commands.dungeon.event.bossDungeon;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.selections.SelectOption;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BossDungeonMainSelectEvent {

    public void execute(StringSelectInteractionEvent event) {
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
                .setTitle("보스 던전")
                .setDescription(
                        """
                        보스 던전에는 일반 몬스터보다
                        훨씬 강력한 몬스터들이 존재합니다.
                        
                        던전 입장에 필요한 재료는 해당
                        던전에 맞는 일반 몬스터들에게
                        낮은 확률로 드랍됩니다.
                        """
                )
                .build();
    }

    private StringSelectMenu buildMenu() {
        return StringSelectMenu.create("bossDungeonMenu")
                .setPlaceholder("던전 선택")
                .addOptions(Arrays.asList(
                        SelectOption.of("평야의 끝자락", "dungeon-boss-B01"),
                        SelectOption.of("요정왕의 쉼터", "dungeon-boss-B02"),
                        SelectOption.of("빠져나올 수 없는 늪", "dungeon-boss-B03"),
                        SelectOption.of("언덕의 정상", "dungeon-boss-B04"),
                        SelectOption.of("왕의 알현실", "dungeon-boss-B05"),
                        SelectOption.of("땅이 흔들리는 곳", "dungeon-boss-B06"),
                        SelectOption.of("지하의 밑바닥", "dungeon-boss-B07"),
                        SelectOption.of("부유 신전", "dungeon-boss-B08"),
                        SelectOption.of("드래곤의 둥지", "dungeon-boss-B09"),
                        SelectOption.of("차원의 틈새", "dungeon-boss-B10")
                ))
                .build();
    }
}
