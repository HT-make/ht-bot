package com.htmake.htbot.discord.commands.battle.event;

import com.htmake.htbot.discord.commands.dungeon.data.GetItem;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.json.JSONObject;

import java.awt.*;
import java.util.List;

public class BattleResultEvent {

    public void execute(
            ButtonInteractionEvent event,
            JSONObject levelUp,
            JSONObject monsterLoot,
            List<GetItem> getItemList
    ) {
        Message message = event.getMessage();

        String levelUpMessage = levelUp.getBoolean("levelUp") ? ":up: 레벨업!!" : "";

        StringBuilder getItemMessage = new StringBuilder();

        if (getItemList.size() > 0) {
            for (GetItem getItem : getItemList) {
                getItemMessage.append(getItem.getName()).append("\n");
            }
        } else {
            getItemMessage.append("획득한 아이템이 없습니다.");
        }

        MessageEmbed newEmbed = new EmbedBuilder()
                .setColor(Color.GREEN)
                .setTitle(":crossed_swords: 전투 승리!")
                .setDescription(levelUpMessage)
                .addField(":sparkles: 획득 경험치", "" + monsterLoot.getInt("exp"), true)
                .addField(":coin: 획득 골드", "" + monsterLoot.get("gold"), true)
                .addField(":purse: 획득 아이템", getItemMessage.toString(), false)
                .build();

        message.editMessageEmbeds(newEmbed)
                .setActionRow(
                        Button.success("dungeon-next", "전진하기"),
                        Button.danger("dungeon-close", "돌아가기")
                )
                .queue();
    }
}
