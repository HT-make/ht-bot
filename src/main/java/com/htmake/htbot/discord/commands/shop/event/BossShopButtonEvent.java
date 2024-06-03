package com.htmake.htbot.discord.commands.shop.event;

import com.htmake.htbot.discord.commands.shop.cache.BossShopCache;
import com.htmake.htbot.discord.commands.shop.data.BossShopItem;
import com.htmake.htbot.discord.commands.shop.util.ShopUtil;
import com.htmake.htbot.discord.util.ErrorUtil;
import com.htmake.htbot.global.cache.CacheFactory;
import com.htmake.htbot.global.unirest.HttpClient;
import com.htmake.htbot.global.unirest.impl.HttpClientImpl;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.selections.SelectOption;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;
import org.json.JSONArray;
import org.json.JSONObject;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class BossShopButtonEvent {

    private final HttpClient httpClient;
    private final ErrorUtil errorUtil;
    private final ShopUtil shopUtil;

    private final BossShopCache bossShopCache;

    public BossShopButtonEvent() {
        this.httpClient = new HttpClientImpl();
        this.errorUtil = new ErrorUtil();
        this.shopUtil = new ShopUtil();

        this.bossShopCache = CacheFactory.bossShopCache;
    }

    public void execute(ButtonInteractionEvent event) {
        HttpResponse<JsonNode> response = request();

        if (response.getStatus() == 200)  {
            JSONObject bossShopObject = response.getBody().getObject();
            requestSuccess(event, bossShopObject);

        } else {
            errorUtil.sendError(event.getHook(), "보스 상점", "보스 상점을 불러오는데 실패했습니다.");
        }
    }

    private HttpResponse<JsonNode> request() {
        String endPoint = "/shop/boss/list";
        return httpClient.sendGetRequest(endPoint);
    }

    private void requestSuccess(ButtonInteractionEvent event, JSONObject bossShopObject) {
        JSONArray bossShopArray = bossShopObject.getJSONArray("itemList");
        List<BossShopItem> bossShopItemList = toBossShopItemList(bossShopArray);

        bossShopCache.put(event.getUser().getId(), bossShopItemList);

        MessageEmbed newEmbed = buildEmbed(bossShopItemList, event.getMessage().getEmbeds().get(0));

        int size = bossShopItemList.size();
        int page = (size % 10 == 0 ? size / 10 : size / 10 + 1);

        List<ActionRow> actionRowList = new ArrayList<>();
        StringSelectMenu selectMenu = menuEmbed("구매할 품목을 선택해주세요.", bossShopItemList);
        List<Button> buttonList = buttonEmbed(page);

        actionRowList.add(ActionRow.of(selectMenu));
        actionRowList.add(ActionRow.of(buttonList));

        event.getHook().editOriginalEmbeds(newEmbed)
                .setComponents(actionRowList)
                .queue();
    }

    private MessageEmbed buildEmbed(List<BossShopItem> bossShopItemList, MessageEmbed embed) {
        List<BossShopItem> bossShopItemListToLeftPage = new ArrayList<>();
        List<BossShopItem> bossShopItemListToRightPage = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            bossShopItemListToLeftPage.add(bossShopItemList.get(i));
        }
        for (int i = 5; i < 10; i++) {
            bossShopItemListToRightPage.add(bossShopItemList.get(i));
        }

        EmbedBuilder embedBuilder = new EmbedBuilder()
                .setColor(Color.GREEN)
                .setAuthor(embed.getAuthor().getName(), null, embed.getAuthor().getIconUrl())
                .setTitle(":dragon: 보스 상점")
                .addField("", shopUtil.format(bossShopItemListToLeftPage), true)
                .addField("", shopUtil.format(bossShopItemListToRightPage), true);

        return embedBuilder.build();
    }

    public List<Button> buttonEmbed(int page) {
        List<Button> buttonList = new ArrayList<>();
        Button cancelButton = Button.danger("cancel", "닫기");

        Button leftButton = Button.primary("shop-page-boss-0", "◄").asDisabled();
        Button pageButton = Button.secondary("blank", "1/" + page).asDisabled();
        Button rightButton = Button.primary("shop-page-boss-2", "►");

        buttonList.add(leftButton);
        buttonList.add(pageButton);
        buttonList.add(rightButton);

        buttonList.add(cancelButton);

        return buttonList;
    }

    public static StringSelectMenu menuEmbed(String placeholder, List<BossShopItem> bossShopItemList) {
        List<SelectOption> options = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            if (bossShopItemList.size() < i) break;
            BossShopItem bossShopItem = bossShopItemList.get(i);
            options.add(SelectOption.of(bossShopItem.getName(), "shop-purchase-boss-" + bossShopItem.getId()));
        }

        return StringSelectMenu.create("bossShopMenu")
                .setPlaceholder(placeholder)
                .addOptions(options)
                .build();
    }

    public List<BossShopItem> toBossShopItemList(JSONArray bossShopItemArray) {
        List<BossShopItem> bossShopItemList = new ArrayList<>();

        for (int i = 0; i < bossShopItemArray.length(); i++) {
            JSONObject bossShopItemObject = bossShopItemArray.getJSONObject(i);

            BossShopItem bossShopItem = BossShopItem.builder()
                    .id(bossShopItemObject.getString("id"))
                    .name(bossShopItemObject.getString("name"))
                    .coin(bossShopItemObject.getInt("coin"))
                    .build();

            bossShopItemList.add(bossShopItem);
        }

        return bossShopItemList;
    }
}
