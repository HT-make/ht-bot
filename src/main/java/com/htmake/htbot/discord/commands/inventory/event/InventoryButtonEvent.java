package com.htmake.htbot.discord.commands.inventory.event;

import com.htmake.htbot.discord.commands.inventory.cache.InventoryCache;
import com.htmake.htbot.discord.commands.inventory.data.Inventory;
import com.htmake.htbot.discord.commands.inventory.data.InventoryItem;
import com.htmake.htbot.discord.commands.inventory.util.InventoryUtil;
import com.htmake.htbot.discord.util.ErrorUtil;
import com.htmake.htbot.global.cache.CacheFactory;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;

import java.util.ArrayList;
import java.util.List;

public class InventoryButtonEvent {

    private final ErrorUtil errorUtil;
    private final InventoryUtil inventoryUtil;

    private final InventoryCache inventoryCache;

    public InventoryButtonEvent() {
        this.errorUtil = new ErrorUtil();
        this.inventoryUtil = new InventoryUtil();

        this.inventoryCache = CacheFactory.inventoryCache;
    }

    public void execute(ButtonInteractionEvent event, int page) {
        MessageEmbed embed = event.getMessage().getEmbeds().get(0);

        Inventory inventory = inventoryCache.get(event.getUser().getId());

        if (inventory == null) {
            errorUtil.sendError(event.getMessage(), "인벤토리", "인벤토리를 찾을 수 없습니다.");
            return;
        }

        String convertedCategory = inventoryUtil.getCategory(inventory.getCategory());

        MessageEmbed newEmbed = buildEmbed(embed, page, inventory, convertedCategory);

        List<ActionRow> actionRowList = new ArrayList<>();

        List<Button> buttonList = buttonEmbed(page);
        StringSelectMenu menu = InventoryUtil.createInventoryMenu("현재 페이지 : " + convertedCategory);

        ActionRow actionRowButton = ActionRow.of(buttonList);
        ActionRow actionRowSelect = ActionRow.of(menu);

        actionRowList.add(actionRowButton);
        actionRowList.add(actionRowSelect);

        event.getMessage().editMessageEmbeds(newEmbed)
                .setComponents(actionRowList)
                .queue();
    }
    private MessageEmbed buildEmbed(MessageEmbed embed, int page, Inventory inventory, String convertedCategory) {
        EmbedBuilder embedBuilder = inventoryUtil.embedBuilder(embed, inventory);

        List<InventoryItem> inventoryItemList = inventory.getInventoryItemList();
        List<InventoryItem> categoryItemList = inventory.getCategoryItemList();

        if (convertedCategory.equals("전체")){
            embedBuilder = inventoryUtil.addItemField(embedBuilder, inventoryItemList, page);
        } else {
            embedBuilder = inventoryUtil.addItemField(embedBuilder, categoryItemList, page);
        }

        return embedBuilder.build();
    }

    public List<Button> buttonEmbed(int page) {
        List<Button> buttonList = new ArrayList<>();

        String leftId = "inventory-left-" + page;
        String pageLabel = page + "/5";
        String rightId = "inventory-right-" + (page + 1);

        Button pageButton = Button.secondary("blank", pageLabel).asDisabled();
        Button leftButton = Button.primary(leftId, "◄");
        Button rightButton = Button.primary(rightId, "►");
        Button cancelButton = Button.danger("cancel", "닫기");

        if (page == 1) {
            buttonList.add(leftButton.asDisabled());
            buttonList.add(pageButton);
            buttonList.add(rightButton);
            buttonList.add(cancelButton);
        } else if (page == 5) {
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
}
