package com.htmake.htbot.discord.commands.shop.event;

import com.htmake.htbot.discord.util.ErrorUtil;
import com.htmake.htbot.domain.shop.entity.RandomShop;
import com.htmake.htbot.global.unirest.HttpClient;
import com.htmake.htbot.global.unirest.impl.HttpClientImpl;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import kotlin.Pair;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.json.JSONArray;
import org.json.JSONObject;

import java.awt.*;
import java.util.*;
import java.util.List;

@Slf4j
public class RandomShopEvent {

    private final HttpClient httpClient;
    private final ErrorUtil errorUtil;

    private final List<String> itemType = new ArrayList<>() {{
            add(":shield: 방어구");
            add(":crossed_swords: 무기");
    }};

    public RandomShopEvent() {
        this.httpClient = new HttpClientImpl();
        this.errorUtil = new ErrorUtil();
    }

    public void execute(SlashCommandInteractionEvent event) {
        String endPoint = "/shop/random/list";

        HttpResponse<JsonNode> response = httpClient.sendGetRequest(endPoint);

        if (response.getStatus() == 200) {
            JSONObject randomShopObject = response.getBody().getObject();

            JSONArray randomShopArray = randomShopObject.getJSONArray("itemList");

            List<RandomShop> randomShopList = toRandomShopList(randomShopArray);
            randomShopList.sort(Comparator.comparing(RandomShop::getId));

            List<Pair<String, List<RandomShop>>> subItemList = subRandomShopList(randomShopList);

            MessageEmbed embed = buildEmbed(subItemList);

            event.replyEmbeds(embed)
                    .addActionRow(Button.danger("cancel", "닫기"))
                    .queue();
        } else {
            String description = response.getBody().getObject().getString("message");
            errorUtil.sendError(event, "랜덤 상점", description);
        }
    }

    private MessageEmbed buildEmbed(List<Pair<String, List<RandomShop>>> subItemList) {
        EmbedBuilder embedBuilder = new EmbedBuilder()
                .setColor(Color.GREEN)
                .setTitle(":game_die: 랜덤 상점")
                .setDescription("매시간마다 아이템이 랜덤으로 변경됩니다.");

        for (Pair<String, List<RandomShop>> itemList : subItemList) {
            embedBuilder.addField(itemList.getFirst(), format(itemList.getSecond()), true);
        }

        return embedBuilder.build();
    }

    private String format(List<RandomShop> itemList) {
        StringBuilder sb = new StringBuilder();

        for (RandomShop item : itemList) {
            String quantity = item.getQuantity() != 0 ? String.valueOf(item.getQuantity()) : "매진";

            sb.append("- ")
                    .append(item.getName())
                    .append(" - ")
                    .append(item.getGold())
                    .append("G (")
                    .append(quantity)
                    .append(")\n");
        }

        return sb.toString();
    }

    private List<RandomShop> toRandomShopList(JSONArray randomShopArray) {
        List<RandomShop> randomShopList = new ArrayList<>();

        for (int i = 0; i < randomShopArray.length(); i++) {
            JSONObject randomShopObject = randomShopArray.getJSONObject(i);

            RandomShop randomShop = RandomShop.builder()
                    .id(randomShopObject.getString("id"))
                    .name(randomShopObject.getString("name"))
                    .quantity(randomShopObject.getInt("quantity"))
                    .gold(randomShopObject.getInt("gold"))
                    .build();

            randomShopList.add(randomShop);
        }

        return randomShopList;
    }

    private List<Pair<String, List<RandomShop>>> subRandomShopList(List<RandomShop> randomShopList) {
        List<Pair<String, List<RandomShop>>> subItemList = new ArrayList<>();

        int partitionSize = 3;
        for (int i = 0; i < randomShopList.size(); i += partitionSize) {
            String type = itemType.get(i / 3);
            List<RandomShop> subItem = randomShopList.subList(i, i + partitionSize);

            subItemList.add(new Pair<>(type, subItem));
        }

        return subItemList;
    }

}