package com.htmake.htbot.discord.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Component
public class GlobalCommand extends ListenerAdapter {

    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
        event.deferEdit().queue();
        String component = event.getComponentId();
        User user = event.getUser();

        if (component.equals("cancel")) {
            Message message = event.getMessage();
            MessageEmbed embed = message.getEmbeds().get(0);
            String author = Objects.requireNonNull(embed.getAuthor()).getName();

            if (!user.getName().equals(author)) {
                InteractionHook hook = event.getHook().setEphemeral(true);
                hook.sendMessage("이 버튼은 이용할 수 없습니다!").queue();
            } else {
                message.editMessageComponents(Collections.emptyList()).queue();
                message.editMessageEmbeds(new EmbedBuilder()
                        .setColor(Color.ORANGE)
                        .setDescription("작업이 취소되었습니다.")
                        .build()
                ).queue();
            }
        }
    }

    @Override
    public void onStringSelectInteraction(StringSelectInteractionEvent event) {
        event.deferEdit().queue();
    }

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        List<CommandData> commandData = List.of(
                Commands.slash("게임-가입", "게임 기능을 사용하기 위해 가입합니다."),

                Commands.slash("유저-정보", "유저 정보를 검색합니다.").addOptions(selectUser()),

                Commands.slash("던전-입장", "던전에 입장합니다."),

                Commands.slash("인벤토리", "인벤토리를 확인합니다."),

                Commands.slash("상점", "상점에 입장합니다."),
                Commands.slash("랜덤-상점", "랜덤 상점에 입장합니다."),
                Commands.slash("랜덤-상점-구매", "랜덤 상점에서 장비를 구입합니다.").addOptions(insertEquipmentName().setRequired(true)),

                Commands.slash("스킬-목록", "사용 가능한 스킬을 확인합니다."),
                Commands.slash("스킬-등록", "스킬을 등록합니다.")
        );

        event.getJDA().updateCommands().addCommands(commandData).queue();
    }

    private OptionData selectUser() {
        return new OptionData(OptionType.USER, "유저", "정보 검색을 원하는 유저를 선택해 주세요!");
    }

    private OptionData insertEquipmentName() {
        return new OptionData(OptionType.STRING, "장비이름", "장비 이름을 입력해 주세요!");
    }
}
