package com.htmake.htbot.discord.commands.battle.event;

import com.htmake.htbot.discord.commands.battle.action.MonsterAttackAction;
import com.htmake.htbot.discord.commands.battle.action.MonsterKillAction;
import com.htmake.htbot.discord.commands.battle.cache.MonsterDataCache;
import com.htmake.htbot.discord.commands.battle.cache.PlayerDataCache;
import com.htmake.htbot.discord.commands.battle.data.MonsterData;
import com.htmake.htbot.discord.commands.battle.data.PlayerData;
import com.htmake.htbot.discord.commands.battle.data.status.extend.MonsterStatus;
import com.htmake.htbot.discord.commands.battle.data.PlayerSkillStatus;
import com.htmake.htbot.discord.commands.battle.data.status.extend.PlayerStatus;
import com.htmake.htbot.discord.commands.battle.util.BattleUtil;
import com.htmake.htbot.discord.skillAction.BasicSkill;
import com.htmake.htbot.discord.skillAction.type.SkillType;
import com.htmake.htbot.discord.util.ErrorUtil;
import com.htmake.htbot.global.cache.CacheFactory;
import kotlin.Pair;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class BattleSkillButtonEvent {

    private final ErrorUtil errorUtil;
    private final BattleUtil battleUtil;
    private final MonsterAttackAction monsterAttackAction;
    private final MonsterKillAction monsterKillAction;

    private final PlayerDataCache playerDataCache;
    private final MonsterDataCache monsterDataCache;

    public BattleSkillButtonEvent() {
        this.errorUtil = new ErrorUtil();
        this.battleUtil = new BattleUtil();
        this.monsterAttackAction = new MonsterAttackAction();
        this.monsterKillAction = new MonsterKillAction();

        this.playerDataCache = CacheFactory.playerDataCache;
        this.monsterDataCache = CacheFactory.monsterDataCache;
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

        if (!playerDataCache.containsKey(playerId)) {
            errorUtil.sendError(event.getHook(), "전투", "스킬을 불러오지 못했습니다.");
            return;
        }

        PlayerStatus playerStatus = playerDataCache.get(playerId).getPlayerStatus();
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

        if (!playerDataCache.containsKey(playerId) || !monsterDataCache.containsKey(playerId)) {
            errorUtil.sendError(event.getHook(), "전투", "정보를 불러오지 못했습니다.");
            return;
        }

        PlayerData playerData = playerDataCache.get(playerId);
        PlayerStatus playerStatus = playerData.getPlayerStatus();

        MonsterData monsterData = monsterDataCache.get(playerId);
        MonsterStatus monsterStatus = monsterData.getMonsterStatus();

        Map<Integer, PlayerSkillStatus> playerSkill = playerStatus.getPlayerSkill();
        PlayerSkillStatus usedSkill = playerSkill.get(number);

        if (usedSkill == null) {
            errorUtil.sendError(event.getHook(), "스킬 사용", "스킬 등록을 해주세요.");
            return;
        }

        BasicSkill basicSkill = usedSkill.getBasicSkill();

        if (!basicSkill.manaCheck(playerData)) {
            errorUtil.sendError(event.getHook(), "스킬 사용", "마나가 부족합니다.");
            return;
        }

        if (battleUtil.conditionCheck(event, playerStatus, monsterStatus)) {
            monsterAttackAction.execute(event, playerStatus, monsterStatus);
            return;
        }

        String message = event.getUser().getName() + "의 " + usedSkill.getName() + ".";
        battleUtil.updateSituation(playerId, message);
        battleUtil.editEmbed(event, playerStatus, monsterStatus, "start");

        List<Pair<String, SkillType>> resultList = basicSkill.execute(playerData, monsterData);

        for (Pair<String, SkillType> result : resultList) {
            switch (result.getSecond()) {
                case ATTACK -> message = result.getFirst() + "의 데미지를 입혔다.";
                case HEAL -> message = result.getFirst() + "의 체력을 회복했다.";
                case BUFF -> message = result.getFirst() + " 효과를 얻었다.";
                case DEBUFF -> message = result.getFirst() + " 효과를 입혔다.";
            }

            battleUtil.updateSituation(playerId, message);
            battleUtil.editEmbed(event, playerStatus, monsterStatus, "progress");
        }

        if (monsterStatus.getHealth() == 0) {
            monsterKillAction.execute(event, playerStatus, monsterStatus);
        } else {
            monsterAttackAction.execute(event, playerStatus, monsterStatus);
        }
    }
}
