package com.htmake.htbot.discord.commands.battle;

import com.htmake.htbot.discord.commands.battle.event.BattleRetreatButtonEvent;
import com.htmake.htbot.discord.commands.battle.event.BattleSkillButtonEvent;
import com.htmake.htbot.discord.commands.battle.event.PlayerAttackButtonEvent;
import com.htmake.htbot.discord.util.ErrorUtil;
import com.htmake.htbot.discord.util.MessageUtil;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BattleCommand extends ListenerAdapter {

    private final PlayerAttackButtonEvent playerAttackButtonEvent;

    private final BattleSkillButtonEvent battleSkillButtonEvent;

    private final BattleRetreatButtonEvent battleRetreatButtonEvent;

    private final ErrorUtil errorUtil;
    private final MessageUtil messageUtil;

    public BattleCommand() {
        this.playerAttackButtonEvent = new PlayerAttackButtonEvent();

        this.battleSkillButtonEvent = new BattleSkillButtonEvent();

        this.battleRetreatButtonEvent = new BattleRetreatButtonEvent();

        this.errorUtil = new ErrorUtil();
        this.messageUtil = new MessageUtil();
    }

    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
        List<String> componentList = List.of(event.getComponentId().split("-"));

        if (componentList.get(0).equals("battle")) {
            if (messageUtil.validCheck(event.getMessage(), event.getUser().getName())) {
                errorUtil.sendError(event.getHook(), "제한된 버튼", "이 버튼은 이용할 수 없습니다.");
                return;
            }

            switch (componentList.get(1)) {
                case "attack" -> playerAttackButtonEvent.execute(event);
                case "skill" -> battleSkillButtonEvent.execute(event, componentList.get(2));
                case "retreat" -> battleRetreatButtonEvent.execute(event);
            }
        }
    }
}