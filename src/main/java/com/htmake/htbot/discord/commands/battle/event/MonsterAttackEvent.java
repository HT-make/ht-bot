package com.htmake.htbot.discord.commands.battle.event;

import com.htmake.htbot.discord.commands.battle.data.MonsterStatus;
import com.htmake.htbot.discord.commands.battle.data.PlayerStatus;
import com.htmake.htbot.discord.commands.battle.util.BattleUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

import java.awt.*;
import java.util.Collections;

public class MonsterAttackEvent {

    private final BattleUtil battleUtil;

    public MonsterAttackEvent() {
        this.battleUtil = new BattleUtil();
    }

    public void execute(ButtonInteractionEvent event, PlayerStatus playerStatus, MonsterStatus monsterStatus) {
        String playerId = event.getUser().getId();

        String message = monsterStatus.getName() + "의 공격.";
        battleUtil.updateSituation(playerId, message);

        battleUtil.editEmbed(event, playerStatus, monsterStatus);

        int damage = Math.max(0, monsterStatus.getDamage() - playerStatus.getDefence());
        playerStatus.setHealth(Math.max(0, playerStatus.getHealth() - damage));

        message = damage + "의 데미지를 입혔다!";
        battleUtil.updateSituation(playerId, message);

        battleUtil.editEmbed(event, playerStatus, monsterStatus);

        if (playerStatus.getHealth() == 0) {
            killPlayer(event, playerStatus, monsterStatus);
        }
    }

    private void killPlayer(ButtonInteractionEvent event, PlayerStatus playerStatus, MonsterStatus monsterStatus) {

        User user = event.getUser();
        String playerId = user.getId();
        String name = user.getName();

        String message = name + "이/가 사망했다.";
        battleUtil.updateSituation(playerId, message);

        battleUtil.editEmbed(event, playerStatus, monsterStatus);

        event.getMessage().editMessageComponents(Collections.emptyList()).queue();
        event.getMessage().editMessageEmbeds(new EmbedBuilder()
                        .setColor(Color.RED)
                        .setTitle(":skull: 전투 패배")
                        .setDescription("전투에서 패배했습니다.")
                        .build()
                )
                .queue();

        battleUtil.removeCurrentBattleCache(playerId);
    }
}
