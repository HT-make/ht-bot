package com.htmake.htbot.discord.commands.shop.event;

import com.htmake.htbot.discord.util.ErrorUtil;
import com.htmake.htbot.discord.util.FormatUtil;
import com.htmake.htbot.domain.shop.entity.RandomShop;
import com.htmake.htbot.global.unirest.HttpClient;
import com.htmake.htbot.global.unirest.impl.HttpClientImpl;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import kotlin.Pair;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.selections.SelectOption;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;
import org.json.JSONArray;
import org.json.JSONObject;

import java.awt.*;
import java.text.DecimalFormat;
import java.util.*;
import java.util.List;

@Slf4j
public class RandomShopButtonEvent {

    private final HttpClient httpClient;
    private final ErrorUtil errorUtil;

    private final List<String> itemType = new ArrayList<>() {{
            add(":shield: 방어구");
            add(":crossed_swords: 무기");
    }};

    public RandomShopButtonEvent() {
        this.httpClient = new HttpClientImpl();
        this.errorUtil = new ErrorUtil();
    }

    public void execute(ButtonInteractionEvent event) {
        HttpResponse<JsonNode> response = request();

        if (response.getStatus() == 200) {
            JSONArray randomShopArray = response.getBody().getObject().getJSONArray("itemList");
            requestSuccess(event, randomShopArray);
        } else {
            String description = response.getBody().getObject().getString("message");
            errorUtil.sendError(event.getHook(), "랜덤 상점", description);
        }
    }

    private HttpResponse<JsonNode> request() {
        String endPoint = "/shop/random/list";
        return httpClient.sendGetRequest(endPoint);
    }

    private void requestSuccess(ButtonInteractionEvent event, JSONArray randomShopArray){
        List<RandomShop> randomShopList = toRandomShopList(randomShopArray);
        randomShopList.sort(Comparator.comparing(RandomShop::getId));

        List<Pair<String, List<RandomShop>>> subItemList = subRandomShopList(randomShopList);

        MessageEmbed embed = buildEmbed(subItemList, event.getUser());

        List<ActionRow> actionRowList = new ArrayList<>();
        StringSelectMenu selectMenu = menuEmbed("구매할 품목을 선택해주세요.", randomShopList);
        List<Button> buttonList = buttonEmbed();

        actionRowList.add(ActionRow.of(selectMenu));
        actionRowList.add(ActionRow.of(buttonList));

        event.getHook().editOriginalEmbeds(embed)
                .setComponents(actionRowList)
                .queue();
    }

    private MessageEmbed buildEmbed(List<Pair<String, List<RandomShop>>> subItemList, User user) {
        String profileUrl = user.getAvatarUrl() != null ? user.getAvatarUrl() : user.getDefaultAvatarUrl();

        EmbedBuilder embedBuilder = new EmbedBuilder()
                .setColor(Color.GREEN)
                .setAuthor(user.getName(), null, profileUrl)
                .setTitle(":game_die: 랜덤 상점")
                .setDescription("매시간마다 아이템이 랜덤으로 변경됩니다.");

        for (Pair<String, List<RandomShop>> itemList : subItemList) {
            embedBuilder.addField(itemList.getFirst(), format(itemList.getSecond()), true);
        }

        return embedBuilder.build();
    }

    public List<Button> buttonEmbed() {
        List<Button> buttonList = new ArrayList<>();
        Button cancelButton = Button.danger("cancel", "닫기");
        buttonList.add(cancelButton);

        return buttonList;
    }

    public static StringSelectMenu menuEmbed(String placeholder, List<RandomShop> randomShopList) {
        List<SelectOption> options = new ArrayList<>();

        for (RandomShop randomShop : randomShopList) {
            options.add(SelectOption.of(randomShop.getName(), "shop-purchase-random-" + randomShop.getName()));
        }

        return StringSelectMenu.create("randomShopMenu")
                .setPlaceholder(placeholder)
                .addOptions(options)
                .build();
    }

    private String format(List<RandomShop> itemList) {
        StringBuilder sb = new StringBuilder();

        for (RandomShop item : itemList) {
            String formattedQuantity = item.getQuantity() != 0 ? FormatUtil.decimalFormat(item.getQuantity()) : "매진";
            String formattedGold = FormatUtil.decimalFormat(item.getGold());

            sb.append("- ")
                    .append(item.getName())
                    .append(" - ")
                    .append(formattedGold)
                    .append("G (")
                    .append(formattedQuantity)
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