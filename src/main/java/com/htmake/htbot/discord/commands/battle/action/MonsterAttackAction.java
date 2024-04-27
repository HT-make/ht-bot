package com.htmake.htbot.discord.commands.battle.action;

import com.htmake.htbot.discord.commands.battle.data.MonsterStatus;
import com.htmake.htbot.discord.commands.battle.data.PlayerStatus;
import com.htmake.htbot.discord.commands.battle.util.BattleUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import org.apache.commons.math3.random.MersenneTwister;
import org.apache.commons.math3.random.RandomGenerator;

import java.awt.*;
import java.util.Collections;

public class MonsterAttackAction {

    private final BattleUtil battleUtil;

    public MonsterAttackAction() {
        this.battleUtil = new BattleUtil();
    }

    public void execute(ButtonInteractionEvent event, PlayerStatus playerStatus, MonsterStatus monsterStatus) {
        String playerId = event.getUser().getId();

        int damage;

        if (monsterStatus.getSkillName() != null && skillChance()) {
            damage = skillAttack(event, playerStatus, monsterStatus);
        } else {
            damage = normalAttack(event, playerStatus, monsterStatus);
        }

        playerStatus.setHealth(Math.max(0, playerStatus.getHealth() - damage));

        String message = damage + "의 데미지를 입혔다!";
        battleUtil.updateSituation(playerId, message);
        battleUtil.editEmbed(event, playerStatus, monsterStatus);

        if (playerStatus.getHealth() == 0) {
            killPlayer(event, playerStatus, monsterStatus);
        }
    }

    private boolean skillChance() {
        RandomGenerator random = new MersenneTwister();
        int skillChance = random.nextInt(100) + 1;
        return skillChance <= 20;
    }

    private int skillAttack(ButtonInteractionEvent event, PlayerStatus playerStatus, MonsterStatus monsterStatus) {
        String message = monsterStatus.getName() + "의 " + monsterStatus.getSkillName() + "!";
        battleUtil.updateSituation(event.getUser().getId(), message);
        battleUtil.editEmbed(event, playerStatus, monsterStatus);

        return Math.max(0, monsterStatus.getSkillDamage() - playerStatus.getDefence());
    }

    private int normalAttack(ButtonInteractionEvent event, PlayerStatus playerStatus, MonsterStatus monsterStatus) {
        String message = monsterStatus.getName() + "의 공격.";
        battleUtil.updateSituation(event.getUser().getId(), message);
        battleUtil.editEmbed(event, playerStatus, monsterStatus);

        return Math.max(0, monsterStatus.getDamage() - playerStatus.getDefence());
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
