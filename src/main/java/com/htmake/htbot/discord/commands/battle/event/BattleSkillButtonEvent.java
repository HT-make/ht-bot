package com.htmake.htbot.discord.commands.battle.event;

import com.htmake.htbot.discord.commands.battle.action.MonsterAttackAction;
import com.htmake.htbot.discord.commands.battle.action.MonsterKillAction;
import com.htmake.htbot.discord.commands.battle.cache.MonsterStatusCache;
import com.htmake.htbot.discord.commands.battle.cache.PlayerStatusCache;
import com.htmake.htbot.discord.commands.battle.data.MonsterStatus;
import com.htmake.htbot.discord.commands.battle.data.PlayerSkillStatus;
import com.htmake.htbot.discord.commands.battle.data.PlayerStatus;
import com.htmake.htbot.discord.commands.battle.util.BattleUtil;
import com.htmake.htbot.discord.util.ErrorUtil;
import com.htmake.htbot.global.cache.CacheFactory;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.apache.commons.math3.random.MersenneTwister;
import org.apache.commons.math3.random.RandomGenerator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class BattleSkillButtonEvent {

    private final ErrorUtil errorUtil;
    private final BattleUtil battleUtil;
    private final MonsterAttackAction monsterAttackAction;
    private final MonsterKillAction monsterKillAction;

    private final PlayerStatusCache playerStatusCache;
    private final MonsterStatusCache monsterStatusCache;

    public BattleSkillButtonEvent() {
        this.errorUtil = new ErrorUtil();
        this.battleUtil = new BattleUtil();
        this.monsterAttackAction = new MonsterAttackAction();
        this.monsterKillAction = new MonsterKillAction();

        this.playerStatusCache = CacheFactory.playerStatusCache;
        this.monsterStatusCache = CacheFactory.monsterStatusCache;
    }

    public void execute(ButtonInteractionEvent event, String option) {
        if (option.equals("open")) {
            openSkill(event);
        } else if (option.equals("close")) {
            closeSkill(event);
        } else {
            useSkill(event, Integer.parseInt(option));
        }
    }

    private void openSkill(ButtonInteractionEvent event) {
        String playerId = event.getUser().getId();

        if (!playerStatusCache.containsKey(playerId)) {
            errorUtil.sendError(event.getHook(), "전투", "스킬을 불러오지 못했습니다.");
            return;
        }

        PlayerStatus playerStatus = playerStatusCache.get(playerId);
        Map<Integer, PlayerSkillStatus> playerSkill = playerStatus.getPlayerSkill();

        List<String> skillNameList = new ArrayList<>();

        for (int i = 1; i <= 5; i++) {
            String skillName = "스킬" + i;

            if (playerSkill.containsKey(i)) {
                skillName = playerSkill.get(i).getName();
            }

            skillNameList.add(skillName);
        }

        List<ActionRow> actionRowList = new ArrayList<>();

        List<Button> firstButtonList = new ArrayList<>(Arrays.asList(
                Button.primary("battle-skill-1", skillNameList.get(0)),
                Button.primary("battle-skill-2", skillNameList.get(1)),
                Button.primary("battle-skill-3", skillNameList.get(2))
        ));

        List<Button> secondButtonList = new ArrayList<>(Arrays.asList(
                Button.primary("battle-skill-4", skillNameList.get(3)),
                Button.primary("battle-skill-5", skillNameList.get(4)),
                Button.danger("battle-skill-close", "닫기")
        ));

        actionRowList.add((ActionRow.of(firstButtonList)));
        actionRowList.add((ActionRow.of(secondButtonList)));

        event.getHook().editOriginalComponents(actionRowList).queue();
    }

    private void closeSkill(ButtonInteractionEvent event) {
        List<Button> buttonList = new ArrayList<>(Arrays.asList(
                Button.success("battle-attack", "공격"),
                Button.primary("battle-skill-open", "스킬"),
                Button.primary("battle-potion-open", "포션"),
                Button.danger("battle-retreat", "후퇴")
        ));

        event.getHook().editOriginalComponents(ActionRow.of(buttonList)).queue();
    }

    private void useSkill(ButtonInteractionEvent event, int number) {
        String playerId = event.getUser().getId();

        if (!playerStatusCache.containsKey(playerId) || !monsterStatusCache.containsKey(playerId)) {
            errorUtil.sendError(event.getHook(), "전투", "정보를 불러오지 못했습니다.");
            return;
        }

        PlayerStatus playerStatus = playerStatusCache.get(playerId);
        MonsterStatus monsterStatus = monsterStatusCache.get(playerId);

        Map<Integer, PlayerSkillStatus> playerSkill = playerStatus.getPlayerSkill();
        PlayerSkillStatus usedSkill = playerSkill.get(number);

        if (usedSkill == null) {
            errorUtil.sendError(event.getHook(), "스킬 사용", "스킬 등록을 해주세요.");
            return;
        }

        if (playerStatus.getMana() < usedSkill.getMana()) {
            errorUtil.sendError(event.getHook(), "스킬 사용", "마나가 부족합니다.");
            return;
        }

        playerStatus.setMana(playerStatus.getMana() - usedSkill.getMana());

        String message = event.getUser().getName() + "의 " + usedSkill.getName() + ".";
        battleUtil.updateSituation(playerId, message);
        battleUtil.editEmbed(event, playerStatus, monsterStatus, "start");

        int skillDamage = skillDamage(playerStatus, monsterStatus, usedSkill.getValue());
        monsterStatus.setHealth(Math.max(0, monsterStatus.getHealth() - skillDamage));

        message = skillDamage + "의 데미지를 입혔다.";
        battleUtil.updateSituation(playerId, message);
        battleUtil.editEmbed(event, playerStatus, monsterStatus, "progress");

        if (monsterStatus.getHealth() == 0) {
            monsterKillAction.execute(event, playerStatus, monsterStatus);
        } else {
            monsterAttackAction.execute(event, playerStatus, monsterStatus);
        }
    }

    private int skillDamage(PlayerStatus playerStatus, MonsterStatus monsterStatus, int value) {
        RandomGenerator random = new MersenneTwister();
        int randomNum = random.nextInt(100);

        double skillValueMultiple = (double) value / 100;

        int damage = (int) (playerStatus.getDamage() * skillValueMultiple);
        int criticalChance = playerStatus.getCriticalChance();
        int criticalDamage = playerStatus.getCriticalDamage();

        int monsterDefence = monsterStatus.getDefence();

        if (randomNum < criticalChance) {
            double criticalDamageMultiple = (double) criticalDamage / 100;
            damage = (int) (damage * criticalDamageMultiple);
        }

        damage -= monsterDefence;
        return damage;
    }
}
