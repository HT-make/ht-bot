package com.htmake.htbot.discord.commands.battle.action;

import com.htmake.htbot.discord.commands.battle.util.BattleActionUtil;
import com.htmake.htbot.discord.commands.dungeon.data.DungeonPlayer;
import com.htmake.htbot.discord.util.ErrorUtil;
import com.htmake.htbot.global.cache.CacheFactory;
import com.htmake.htbot.discord.commands.dungeon.cache.FieldDungeonStatusCache;
import com.htmake.htbot.discord.commands.dungeon.data.GetItem;
import com.htmake.htbot.discord.commands.dungeon.data.FieldDungeonStatus;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;
import java.util.List;

public class FieldBattleResultAction {

    private final ErrorUtil errorUtil;
    private final BattleActionUtil battleActionUtil;

    private final FieldDungeonStatusCache fieldDungeonStatusCache;

    public FieldBattleResultAction() {
        this.errorUtil = new ErrorUtil();
        this.battleActionUtil = new BattleActionUtil();

        this.fieldDungeonStatusCache = CacheFactory.fieldDungeonStatusCache;
    }

    public void execute(ButtonInteractionEvent event, String monsterId) {
        JSONObject monsterLoot = battleActionUtil.getMonsterLoot(monsterId, false);

        if (monsterLoot == null) {
            errorUtil.sendError(event.getHook(), "전투", "보상을 획득하지 못했습니다.");
            return;
        }

        HttpResponse<JsonNode> response = battleActionUtil.request(event.getUser().getId(), monsterLoot, false);

        if (response.getStatus() == 200) {
            boolean levelUp = response.getBody().getObject().getBoolean("levelUp");
            requestSuccess(event, monsterLoot, levelUp);
        } else {
            errorUtil.sendError(event.getHook(), "전투", "보상을 획득하지 못했습니다.");
        }
    }

    private void requestSuccess(ButtonInteractionEvent event, JSONObject monsterLoot, boolean levelUp) {
        String playerId = event.getUser().getId();
        FieldDungeonStatus fieldDungeonStatus = fieldDungeonStatusCache.get(playerId);

        List<GetItem> getItemList = getNewGetItemList(playerId, monsterLoot, fieldDungeonStatus);
        MessageEmbed embed = battleActionUtil.buildEmbed(monsterLoot, getItemList, levelUp, event.getUser(), false);

        List<Button> buttonList = new ArrayList<>();
        int stage = fieldDungeonStatus.getStage();

        if (stage != 10) buttonList.add(Button.success("dungeon-field-next", "전진하기"));
        buttonList.add(Button.danger("dungeon-field-close", "돌아가기"));

        event.getHook().editOriginalEmbeds(embed)
                .setActionRow(buttonList)
                .queue();

        if (levelUp) {
            updateDungeonPlayerLevel(playerId, fieldDungeonStatus);
        }
    }

    private List<GetItem> getNewGetItemList(String playerId, JSONObject monsterLoot, FieldDungeonStatus fieldDungeonStatus) {
        JSONArray dropItemArray = monsterLoot.getJSONArray("dropItemList");

        List<GetItem> currentGetItemList = fieldDungeonStatus.getGetItemList();
        List<GetItem> newGetItemList = battleActionUtil.randomDropItemList(dropItemArray);
        currentGetItemList.addAll(newGetItemList);

        fieldDungeonStatus.setGetItemList(currentGetItemList);
        fieldDungeonStatusCache.put(playerId, fieldDungeonStatus);

        return newGetItemList;
    }

    private void updateDungeonPlayerLevel(String playerId, FieldDungeonStatus fieldDungeonStatus) {
        DungeonPlayer dungeonPlayer = fieldDungeonStatus.getDungeonPlayer();
        dungeonPlayer.setLevel(dungeonPlayer.getLevel() + 1);
        fieldDungeonStatus.setDungeonPlayer(dungeonPlayer);
        fieldDungeonStatusCache.put(playerId, fieldDungeonStatus);
    }
}
