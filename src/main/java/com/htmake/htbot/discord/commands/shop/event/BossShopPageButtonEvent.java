package com.htmake.htbot.discord.commands.shop.event;

import com.htmake.htbot.discord.commands.shop.cache.BossShopCache;
import com.htmake.htbot.discord.commands.shop.data.BossShopItem;
import com.htmake.htbot.discord.commands.shop.util.ShopUtil;
import com.htmake.htbot.discord.util.ErrorUtil;
import com.htmake.htbot.global.cache.CacheFactory;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.selections.SelectOption;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class BossShopPageButtonEvent {

    private final ErrorUtil errorUtil;
    private final ShopUtil shopUtil;

    private final BossShopCache bossShopCache;

    public BossShopPageButtonEvent(){
        this.errorUtil = new ErrorUtil();
        this.shopUtil = new ShopUtil();

        this.bossShopCache = CacheFactory.bossShopCache;
    }


    public void execute(ButtonInteractionEvent event, int page) {
        MessageEmbed embed = event.getMessage().getEmbeds().get(0);

        List<BossShopItem> bossShopItemList = bossShopCache.get(event.getUser().getId());

        if (bossShopItemList == null){
            errorUtil.sendError(event.getHook(), "보스 상점", "보스 상점을 찾을 수 없습니다.");
        }

        int size = bossShopItemList.size();
        int maxPage = (size % 10 == 0 ? size / 10 : size / 10 + 1);

        MessageEmbed newEmbed = buildEmbed(embed, page, bossShopItemList);

        List<ActionRow> actionRowList = new ArrayList<>();
        StringSelectMenu selectMenu = menuEmbed("구매할 품목을 선택해주세요.", bossShopItemList, page);
        List<Button> buttonList = buttonEmbed(page, maxPage);

        actionRowList.add(ActionRow.of(selectMenu));
        actionRowList.add(ActionRow.of(buttonList));

        event.getHook().editOriginalEmbeds(newEmbed)
                .setComponents(actionRowList)
                .queue();
    }

    private MessageEmbed buildEmbed(MessageEmbed embed, int page, List<BossShopItem> bossShopItemList) {
        List<BossShopItem> bossShopItemListToLeftPage = new ArrayList<>();
        List<BossShopItem> bossShopItemListToRightPage = new ArrayList<>();

        for (int i = (page - 1) * 10; i < Math.min(page * 10, bossShopItemList.size()); i++) {
            if (i % 2 == 0){
                bossShopItemListToLeftPage.add(bossShopItemList.get(i));
            } else {
                bossShopItemListToRightPage.add(bossShopItemList.get(i));
            }
        }

        EmbedBuilder embedBuilder = new EmbedBuilder()
                .setColor(Color.GREEN)
                .setAuthor(embed.getAuthor().getName(), null, embed.getAuthor().getIconUrl())
                .setTitle(":dragon: 보스 상점")
                .addField("", shopUtil.format(bossShopItemListToLeftPage), true)
                .addField("", shopUtil.format(bossShopItemListToRightPage), true);

        return embedBuilder.build();
    }

    public List<Button> buttonEmbed(int page, int maxPage) {
        List<Button> buttonList = new ArrayList<>();

        Button leftButton = Button.primary("shop-page-boss-" + (page - 1), "◄");
        Button pageButton = Button.secondary("blank", page + "/" + maxPage).asDisabled();
        Button rightButton = Button.primary("shop-page-boss-" + (page + 1), "►");
        Button cancelButton = Button.danger("cancel", "닫기");

        if (page == 1) {
            buttonList.add(leftButton.asDisabled());
            buttonList.add(pageButton);
            buttonList.add(rightButton);
            buttonList.add(cancelButton);
        } else if (page == maxPage) {
            buttonList.add(leftButton);
            buttonList.add(pageButton);
            buttonList.add(rightButton.asDisabled());
            buttonList.add(cancelButton);
        } else {
            buttonList.add(leftButton.asEnabled());
            buttonList.add(pageButton);
            buttonList.add(rightButton.asEnabled());
            buttonList.add(cancelButton);
        }

        return buttonList;
    }

    public static StringSelectMenu menuEmbed(String placeholder, List<BossShopItem> bossShopItemList, int page) {
        List<SelectOption> options = new ArrayList<>();

        for (int i = (page - 1) * 10; i < Math.min(page * 10, bossShopItemList.size()); i++) {
            BossShopItem bossShopItem = bossShopItemList.get(i);
            options.add(SelectOption.of(bossShopItem.getName(), "shop-purchase-boss-" + bossShopItem.getId()));
        }

        return StringSelectMenu.create("bossShopMenu")
                .setPlaceholder(placeholder)
                .addOptions(options)
                .build();
    }
}
