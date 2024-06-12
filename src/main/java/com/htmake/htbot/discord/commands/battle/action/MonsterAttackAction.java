package com.htmake.htbot.discord.commands.battle.action;

import com.htmake.htbot.discord.commands.battle.data.status.extend.MonsterStatus;
import com.htmake.htbot.discord.commands.battle.data.status.extend.PlayerStatus;
import com.htmake.htbot.discord.commands.battle.util.BattleUtil;
import com.htmake.htbot.discord.skillAction.condition.Condition;
import com.htmake.htbot.discord.skillAction.condition.extend.Faint;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import org.apache.commons.math3.random.MersenneTwister;
import org.apache.commons.math3.random.RandomGenerator;

import java.util.Map;

public class MonsterAttackAction {

    private final BattleUtil battleUtil;

    private final PlayerKillAction playerKillAction;
    private final ConditionAction conditionAction;

    public MonsterAttackAction() {
        this.battleUtil = new BattleUtil();

        this.playerKillAction = new PlayerKillAction();
        this.conditionAction = new ConditionAction();
    }

    public void execute(ButtonInteractionEvent event, PlayerStatus playerStatus, MonsterStatus monsterStatus) {
        String playerId = event.getUser().getId();

        if (conditionAction(event, playerStatus, monsterStatus)) {
            conditionAction.execute(event);
            return;
        }

        int damage;

        if (!monsterStatus.getSkillName().equals("null") && skillChance()) {
            damage = skillAttack(event, playerStatus, monsterStatus);
        } else {
            damage = normalAttack(event, playerStatus, monsterStatus);
        }

        Map<String, Condition> playerCondition = playerStatus.getConditionMap();

        if (playerCondition.containsKey("invincible")) {
            damage = 0;
            playerCondition.remove("invincible");
        }

        playerStatus.setHealth(Math.max(0, playerStatus.getHealth() - damage));

        String message = damage + "의 데미지를 입혔다.";
        battleUtil.updateSituation(playerId, message);
        battleUtil.editEmbed(event, playerStatus, monsterStatus, "progress");

        if (playerStatus.getHealth() == 0) {
            playerKillAction.execute(event, playerStatus, monsterStatus);
        } else {
            conditionAction.execute(event);
        }
    }

    private boolean conditionAction(ButtonInteractionEvent event, PlayerStatus playerStatus, MonsterStatus monsterStatus) {
        Map<String, Condition> monsterCondition = monsterStatus.getConditionMap();

        if (monsterCondition.containsKey("faint")) {
            Faint faint = (Faint) monsterCondition.get("faint");

            if (faint.applyEffect()) {
                String message = monsterStatus.getName() + "이/가 기절했다.";
                battleUtil.updateSituation(event.getUser().getId(), message);
                battleUtil.editEmbed(event, playerStatus, monsterStatus, "progress");

                return true;
            }
        }

        return false;
    }

    private boolean skillChance() {
        RandomGenerator random = new MersenneTwister();
        int skillChance = random.nextInt(100);
        return skillChance < 20;
    }

    private int skillAttack(ButtonInteractionEvent event, PlayerStatus playerStatus, MonsterStatus monsterStatus) {
        String message = monsterStatus.getName() + "의 " + monsterStatus.getSkillName() + ".";
        battleUtil.updateSituation(event.getUser().getId(), message);
        battleUtil.editEmbed(event, playerStatus, monsterStatus, "progress");

        return Math.max(0, monsterStatus.getSkillDamage() - playerStatus.getDefence());
    }

    private int normalAttack(ButtonInteractionEvent event, PlayerStatus playerStatus, MonsterStatus monsterStatus) {
        String message = monsterStatus.getName() + "의 공격.";
        battleUtil.updateSituation(event.getUser().getId(), message);
        battleUtil.editEmbed(event, playerStatus, monsterStatus, "progress");

        return Math.max(0, monsterStatus.getDamage() - playerStatus.getDefence());
    }
}
