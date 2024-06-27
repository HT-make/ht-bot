package com.htmake.htbot.discord.commands.battle.event;

import com.htmake.htbot.discord.commands.battle.action.MonsterAttackAction;
import com.htmake.htbot.discord.commands.battle.action.MonsterKillAction;
import com.htmake.htbot.discord.commands.battle.data.MonsterData;
import com.htmake.htbot.discord.commands.battle.data.PlayerData;
import com.htmake.htbot.discord.commands.battle.data.status.extend.MonsterOriginalStatus;
import com.htmake.htbot.discord.commands.battle.data.status.extend.PlayerOriginalStatus;
import com.htmake.htbot.discord.skillAction.condition.Condition;
import com.htmake.htbot.discord.skillAction.condition.extend.Dark;
import com.htmake.htbot.discord.skillAction.condition.extend.Light;
import com.htmake.htbot.discord.util.ErrorUtil;
import com.htmake.htbot.discord.util.FormatUtil;
import com.htmake.htbot.discord.util.RandomUtil;
import com.htmake.htbot.domain.player.enums.Job;
import com.htmake.htbot.global.cache.CacheFactory;
import com.htmake.htbot.discord.commands.battle.cache.MonsterDataCache;
import com.htmake.htbot.discord.commands.battle.cache.PlayerDataCache;
import com.htmake.htbot.discord.commands.battle.data.status.extend.MonsterStatus;
import com.htmake.htbot.discord.commands.battle.data.status.extend.PlayerStatus;
import com.htmake.htbot.discord.commands.battle.util.BattleUtil;
import kotlin.Pair;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import org.apache.commons.math3.random.MersenneTwister;
import org.apache.commons.math3.random.RandomGenerator;

import java.util.Map;

public class PlayerAttackButtonEvent {

    private final ErrorUtil errorUtil;
    private final BattleUtil battleUtil;
    private final MonsterKillAction monsterKillAction;
    private final MonsterAttackAction monsterAttackAction;

    private final PlayerDataCache playerDataCache;
    private final MonsterDataCache monsterDataCache;

    public PlayerAttackButtonEvent() {
        this.errorUtil = new ErrorUtil();
        this.battleUtil = new BattleUtil();
        this.monsterKillAction = new MonsterKillAction();
        this.monsterAttackAction = new MonsterAttackAction();

        this.playerDataCache = CacheFactory.playerDataCache;
        this.monsterDataCache = CacheFactory.monsterDataCache;
    }

    public void execute(ButtonInteractionEvent event) {
        String playerId = event.getUser().getId();

        if (!playerDataCache.containsKey(playerId) || !monsterDataCache.containsKey(playerId)) {
            errorUtil.sendError(event.getHook(), "전투", "정보를 불러오지 못했습니다.");
            return;
        }

        PlayerData playerData = playerDataCache.get(playerId);
        PlayerStatus playerStatus = playerData.getPlayerStatus();
        PlayerOriginalStatus playerOriginalStatus = playerData.getPlayerOriginalStatus();
        MonsterData monsterData = monsterDataCache.get(playerId);
        MonsterStatus monsterStatus = monsterData.getMonsterStatus();
        MonsterOriginalStatus monsterOriginalStatus = monsterData.getMonsterOriginalStatus();

        if (battleUtil.conditionCheck(event, playerStatus, monsterStatus)) {
            monsterAttackAction.execute(event, playerStatus, monsterStatus);
            return;
        }

        playerTurn(event, playerStatus, playerOriginalStatus, monsterStatus, monsterOriginalStatus);

        if (monsterStatus.getHealth() == 0) {
            monsterKillAction.execute(event, playerStatus, monsterStatus);
        } else {
            monsterAttackAction.execute(event, playerStatus, monsterStatus);
        }
    }

    private void playerTurn(
            ButtonInteractionEvent event,
            PlayerStatus playerStatus,
            PlayerOriginalStatus playerOriginalStatus,
            MonsterStatus monsterStatus,
            MonsterOriginalStatus monsterOriginalStatus
    ) {
        Pair<Integer, Boolean> attack = playerAttackDamage(playerStatus, monsterStatus);

        User user = event.getUser();
        String playerId = user.getId();
        String name = user.getName();

        String message = name + "의 ";

        if (attack.getSecond()) {
            message += "치명타 공격!";
        } else {
            message += "공격.";
        }

        battleUtil.updateSituation(playerId, message);
        battleUtil.editEmbed(event, playerStatus, monsterStatus, "start");

        int damage = attack.getFirst();

        Map<String, Condition> monsterCondition = monsterStatus.getConditionMap();

        if (playerStatus.getJob().equals(Job.GREAT_WIZARD)) {
            damage += playerStatus.getMana();
        }

        if (monsterCondition.containsKey("invincible")) {
            damage = 0;
            monsterCondition.remove("invincible");
        }

        monsterStatus.setHealth(Math.max(0, (monsterStatus.getHealth() - damage)));

        message = FormatUtil.decimalFormat(damage) + "의 데미지를 입혔다.";
        battleUtil.updateSituation(playerId, message);
        battleUtil.editEmbed(event, playerStatus, monsterStatus, "progress");


        applyJobSpecificEffects(event, playerStatus, playerOriginalStatus, monsterStatus, monsterOriginalStatus, playerId);
    }

    private void applyJobSpecificEffects(
            ButtonInteractionEvent event,
            PlayerStatus playerStatus,
            PlayerOriginalStatus playerOriginalStatus,
            MonsterStatus monsterStatus,
            MonsterOriginalStatus monsterOriginalStatus,
            String playerId
    ) {
        Job job = playerStatus.getJob();
        Map<String, Condition> playerCondition = playerStatus.getConditionMap();
        Map<String, Condition> monsterCondition = monsterStatus.getConditionMap();

        if (job.equals(Job.SWORD_MASTER) && RandomUtil.randomPercentage(70)) {
            double value = playerCondition.containsKey("sword_god") ? 3.0 : 1.4;
            int damage = (int) (playerStatus.getDamage() * value) - monsterStatus.getDefence();
            monsterStatus.setHealth(Math.max(0, monsterStatus.getHealth() - damage));

            String message = FormatUtil.decimalFormat(damage) + "의 데미지를 입혔다.";
            battleUtil.updateSituation(playerId, message);
            battleUtil.editEmbed(event, playerStatus, monsterStatus, "progress");
        }

        if (job.equals(Job.HOLY_KNIGHT) && RandomUtil.randomPercentage(60)) {
            Light light = playerCondition.containsKey("light") ? (Light) playerCondition.get("light") : new Light();

            if (light.getTurn() < 5) {
                playerCondition.put("light", light);
                light.apply(playerStatus, playerOriginalStatus);
            }

            int originalHealth = playerOriginalStatus.getHealth();
            int healing = (int) (originalHealth * 0.03);
            playerStatus.setHealth(Math.min(originalHealth, playerStatus.getHealth() + healing));

            if (playerCondition.containsKey("angels_protection")) {
                int damage = (playerStatus.getDamage() * 3) - monsterStatus.getDefence();
                monsterStatus.setHealth(Math.max(0, monsterStatus.getHealth() - damage));
            }
        }

        if (job.equals(Job.WIZARD) || job.getLowerJob().contains(Job.WIZARD)) {
            int manaRecovery = (playerCondition.containsKey("realize") ? 50 : 10);
            playerStatus.setMana(playerStatus.getMana() + manaRecovery);
        }

        if (job.equals(Job.BLACK_WIZARD) && RandomUtil.randomPercentage(20)) {
            String message = "어둠 효과를 입혔다.";
            battleUtil.updateSituation(playerId, message);
            battleUtil.editEmbed(event, playerStatus, monsterStatus, "progress");

            if (monsterCondition.containsKey("dark")) {
                Dark dark = (Dark) monsterCondition.get("dark");

                int damage = (playerStatus.getDamage() * 5) - monsterStatus.getDefence();
                monsterStatus.setHealth(Math.max(0, monsterStatus.getHealth() - damage));

                dark.unapply(monsterStatus, monsterOriginalStatus);
                monsterCondition.remove(dark.getId());

                message = FormatUtil.decimalFormat(damage) + "의 데미지를 입혔다.";
                battleUtil.updateSituation(playerId, message);
                battleUtil.editEmbed(event, playerStatus, monsterStatus, "progress");
            } else {
                Dark dark = new Dark();
                dark.apply(monsterStatus, monsterOriginalStatus);
                monsterCondition.put(dark.getId(), dark);
            }
        }
    }

    private Pair<Integer, Boolean> playerAttackDamage(PlayerStatus playerStatus, MonsterStatus monsterStatus) {
        RandomGenerator random = new MersenneTwister();
        int randomNum = random.nextInt(100);

        int damage = playerStatus.getDamage();
        int criticalChance = playerStatus.getCriticalChance();
        int criticalDamage = playerStatus.getCriticalDamage();

        int monsterDefence = monsterStatus.getDefence();

        if (randomNum < criticalChance) {
            double criticalDamageMultiple = (double) criticalDamage / 100;
            damage = (int) (damage * criticalDamageMultiple) - monsterDefence;

            return new Pair<>(Math.max(damage, 0), true);
        }

        damage -= monsterDefence;
        return new Pair<>(Math.max(damage, 0), false);
    }
}
