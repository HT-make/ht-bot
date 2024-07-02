package com.htmake.htbot.discord.commands.battle.action;

import com.htmake.htbot.discord.commands.battle.util.BattleActionUtil;
import com.htmake.htbot.discord.commands.battle.util.BattleUtil;
import com.htmake.htbot.discord.commands.dungeon.data.GetItem;
import com.htmake.htbot.discord.util.ErrorUtil;
import com.htmake.htbot.discord.util.MessageUtil;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public class BossBattleResultAction {

    private final ErrorUtil errorUtil;
    private final BattleUtil battleUtil;
    private final BattleActionUtil battleActionUtil;
    private final MessageUtil messageUtil;

    public BossBattleResultAction() {
        this.errorUtil = new ErrorUtil();
        this.battleUtil = new BattleUtil();
        this.battleActionUtil = new BattleActionUtil();
        this.messageUtil = new MessageUtil();
    }

    public void execute(ButtonInteractionEvent event, String monsterId) {
        JSONObject monsterLoot = battleActionUtil.getMonsterLoot(monsterId, true);

        if (monsterLoot == null) {
            errorUtil.sendError(event.getHook(), "전투", "보상을 획득하지 못했습니다.");
            return;
        }

        HttpResponse<JsonNode> response = battleActionUtil.request(event.getUser().getId(), monsterLoot, true);

        if (response.getStatus() == 200) {
            boolean levelUp = response.getBody().getObject().getBoolean("levelUp");
            requestSuccess(event, monsterLoot, levelUp);
        } else {
            errorUtil.sendError(event.getHook(), "전투", "보상을 획득하지 못했습니다.");
        }
    }

    private void requestSuccess(ButtonInteractionEvent event, JSONObject monsterLoot, boolean levelUp) {
        JSONArray dropItemArray = monsterLoot.getJSONArray("dropItemList");
        List<GetItem> getItemList = battleActionUtil.randomDropItemList(dropItemArray);

        HttpResponse<JsonNode> response = battleUtil.insertGetItemList(getItemList, event.getUser().getId());

        if (response.getStatus() == 200) {
            MessageEmbed embed = battleActionUtil.buildEmbed(monsterLoot, getItemList, levelUp, event.getUser(), true);

            event.getHook().editOriginalEmbeds(embed)
                    .setActionRow(Button.danger("cancel", "닫기"))
                    .queue();

            messageUtil.remove(event.getUser().getId());
        } else {
            errorUtil.sendError(event.getHook(), "전투", "보상을 획득하지 못했습니다.");
        }
    }
}
