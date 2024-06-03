package com.htmake.htbot.discord.commands.global;

import com.htmake.htbot.discord.util.ErrorUtil;
import com.htmake.htbot.discord.util.MessageUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.util.Collections;
import java.util.List;

@Component
public class GlobalCommand extends ListenerAdapter {

    private final ErrorUtil errorUtil;
    private final MessageUtil messageUtil;

    public GlobalCommand() {
        this.errorUtil = new ErrorUtil();
        this.messageUtil = new MessageUtil();
    }

    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
        event.deferEdit().queue();

        String component = event.getComponentId();

        if (component.equals("cancel")) {
            if (messageUtil.validCheck(event.getMessage(), event.getUser().getName())) {
                errorUtil.sendError(event.getHook(), "제한된 버튼", "이 버튼은 이용할 수 없습니다.");
                return;
            }

            messageUtil.remove(event.getUser().getId());

            MessageEmbed embed = new EmbedBuilder()
                    .setColor(Color.GREEN)
                    .setDescription("작업이 취소되었습니다.")
                    .build();

            event.getHook().editOriginalComponents(Collections.emptyList()).queue();
            event.getHook().editOriginalEmbeds(embed).queue();
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
                Commands.slash("상점-판매", "아이템을 판매합니다.").addOptions(selectCategory().setRequired(true), insertName().setRequired(true), insertQuantity().setRequired(true).setRequiredRange(1, 99)),

                Commands.slash("스킬-목록", "사용 가능한 스킬을 확인합니다."),
                Commands.slash("스킬-등록", "스킬을 등록합니다."),

                Commands.slash("퀘스트", "퀘스트를 확인합니다."),

                Commands.slash("도감", "도감을 확인합니다.").addOptions(selectCategory().setRequired(true), insertName().setRequired(true)),

                Commands.slash("장비-장착", "장비를 장착합니다.").addOptions(insertEquipmentName().setRequired(true)),

                Commands.slash("전직", "상위 직업으로 전직합니다.")
        );

        event.getJDA().updateCommands().addCommands(commandData).queue();
    }

    private OptionData selectUser() {
        return new OptionData(OptionType.USER, "유저", "정보 검색을 원하는 유저를 선택해 주세요!");
    }

    private OptionData insertEquipmentName() {
        return new OptionData(OptionType.STRING, "장비이름", "장비 이름을 입력해 주세요!");
    }

    private OptionData selectCategory() {
        return new OptionData(OptionType.STRING, "카테고리", "카테고리를 선택해 주세요!")
                .addChoice("아이템-무기", "weapon")
                .addChoice("아이템-갑옷", "armor")
                .addChoice("아이템-기타", "misc");
    }

    private OptionData insertName() {
        return new OptionData(OptionType.STRING, "이름", "원하시는 옵션의 이름을 입력해 주세요!");
    }

    private OptionData insertQuantity() {
        return new OptionData(OptionType.INTEGER, "수량", "원하시는 수량을 입력해 주세요!");
    }
}
