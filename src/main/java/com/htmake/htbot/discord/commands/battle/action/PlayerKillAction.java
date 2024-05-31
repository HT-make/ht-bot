package com.htmake.htbot.discord.commands.battle.action;

import com.htmake.htbot.discord.commands.battle.data.status.extend.MonsterStatus;
import com.htmake.htbot.discord.commands.battle.data.status.extend.PlayerStatus;
import com.htmake.htbot.discord.commands.battle.util.BattleUtil;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

import java.util.Collections;

public class PlayerKillAction {

    private final BattleUtil battleUtil;

    public PlayerKillAction() {
        this.battleUtil = new BattleUtil();
    }

    public void execute(ButtonInteractionEvent event, PlayerStatus playerStatus, MonsterStatus monsterStatus) {
        User user = event.getUser();
        String playerId = user.getId();
        String name = user.getName();

        String message = name + "이/가 사망했다.";
        battleUtil.updateSituation(playerId, message);
        battleUtil.editEmbed(event, playerStatus, monsterStatus, "progress");

        MessageEmbed embed = battleUtil.battleDefeat(playerId);

        event.getHook().editOriginalComponents(Collections.emptyList()).queue();
        event.getHook().editOriginalEmbeds(embed).queue();

        battleUtil.removeCurrentBattleCache(playerId);
    }
}
