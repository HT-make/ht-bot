package com.htmake.htbot.discord.commands.profession;

import com.htmake.htbot.discord.commands.profession.event.CompletePromotionButtonEvent;
import com.htmake.htbot.discord.commands.profession.event.PromotionButtonEvent;
import com.htmake.htbot.discord.commands.profession.event.ProfessionSlashEvent;
import com.htmake.htbot.discord.commands.profession.event.SelectSecondJobButtonEvent;
import com.htmake.htbot.discord.util.ErrorUtil;
import com.htmake.htbot.discord.util.MessageUtil;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProfessionCommand extends ListenerAdapter {

    private final ProfessionSlashEvent professionSlashEvent;

    private final PromotionButtonEvent promotionButtonEvent;

    private final SelectSecondJobButtonEvent selectSecondJobButtonEvent;

    private final CompletePromotionButtonEvent completePromotionButtonEvent;

    private final ErrorUtil errorUtil;
    private final MessageUtil messageUtil;

    public ProfessionCommand() {
        this.professionSlashEvent = new ProfessionSlashEvent();

        this.promotionButtonEvent = new PromotionButtonEvent();
        this.selectSecondJobButtonEvent = new SelectSecondJobButtonEvent();
        this.completePromotionButtonEvent = new CompletePromotionButtonEvent();

        this.errorUtil = new ErrorUtil();
        this.messageUtil = new MessageUtil();
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        String command = event.getName();

        if (command.equals("전직")) {
            if (messageUtil.validCheck(event.getUser().getId())) {
                errorUtil.sendError(event, "작업 실패", "현재 다른 작업을 진행중입니다.");
                return;
            }

            professionSlashEvent.execute(event);
        }
    }

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        List<String> componentList = List.of(event.getComponentId().split("-"));

        if (componentList.get(0).equals("promotion")) {
            if (messageUtil.validCheck(event.getMessage(), event.getUser().getName())) {
                errorUtil.sendError(event.getHook(), "제한된 버튼", "이 버튼은 이용할 수 없습니다.");
                return;
            }

            if (componentList.get(1).equals("class")) {
                if (componentList.get(2).equals("first")){
                    promotionButtonEvent.execute(event);
                }
                else if (componentList.get(2).equals("second")){
                    selectSecondJobButtonEvent.execute(event);
                }
                else {
                    promotionButtonEvent.execute(event, componentList.get(2));
                }
            }

            if (componentList.get(1).equals("complete")) {
                completePromotionButtonEvent.execute(event, componentList.get(2));
            }
        }
    }
}
