package com.htmake.htbot.discord.commands.inventory.event;

import com.htmake.htbot.discord.commands.inventory.cache.InventoryCache;
import com.htmake.htbot.discord.commands.inventory.data.Inventory;
import com.htmake.htbot.discord.commands.inventory.data.InventoryItem;
import com.htmake.htbot.discord.commands.inventory.util.InventoryUtil;
import com.htmake.htbot.discord.util.ErrorUtil;
import com.htmake.htbot.global.cache.CacheFactory;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;

import java.util.*;
import java.util.List;

public class InventorySelectEvent {

    private final InventoryUtil inventoryUtil;
    private final ErrorUtil errorUtil;

    private final InventoryCache inventoryCache;

    public InventorySelectEvent() {
        this.inventoryUtil = new InventoryUtil();
        this.errorUtil = new ErrorUtil();

        this.inventoryCache = CacheFactory.inventoryCache;
    }

    public void execute(StringSelectInteractionEvent event, String category) {
        MessageEmbed embed = event.getMessage().getEmbeds().get(0);

        Inventory inventory = inventoryCache.get(event.getUser().getId());

        if (inventory == null) {
            errorUtil.sendError(event.getMessage(), "인벤토리", "인벤토리를 찾을 수 없습니다.");
            return;
        }

        String convertedCategory = inventoryUtil.getCategory(category);

        MessageEmbed newEmbed = buildEmbed(embed, inventory, convertedCategory);

        inventory.setCategory(category);
        inventoryCache.put(event.getUser().getId(), inventory);

        List<ActionRow> actionRowList = new ArrayList<>();

        List<Button> buttonList = buildButton();
        StringSelectMenu menu = InventoryUtil.createInventoryMenu("현재 페이지 : " + convertedCategory);

        ActionRow actionRowButton = ActionRow.of(buttonList);
        ActionRow actionRowSelect = ActionRow.of(menu);

        actionRowList.add(actionRowButton);
        actionRowList.add(actionRowSelect);

        event.getMessage().editMessageEmbeds(newEmbed)
                .setComponents(actionRowList)
                .queue();
    }

    private MessageEmbed buildEmbed(MessageEmbed embed, Inventory inventory, String convertedCategory) {
        EmbedBuilder embedBuilder = inventoryUtil.embedBuilder(embed, inventory);

        List<InventoryItem> inventoryItemList = inventory.getInventoryItemList();

        if (convertedCategory.equals("전체")){
            embedBuilder = inventoryUtil.addItemField(embedBuilder, inventoryItemList, 1);
        } else {
            List<InventoryItem> categoryItemList = toCategoryItemList(inventoryItemList, convertedCategory);
            inventory.setCategoryItemList(categoryItemList);

            embedBuilder = inventoryUtil.addItemField(embedBuilder, categoryItemList, 1);
        }

        return embedBuilder.build();
    }

    private List<InventoryItem> toCategoryItemList(List<InventoryItem> inventoryItemList, String category) {
        List<InventoryItem> categoryItemList = new ArrayList<>();
        List<String> prefixes = CATEGORY_PREFIX_MAP.getOrDefault(category, Collections.emptyList());

        for (InventoryItem inventoryItem : inventoryItemList) {
            String id = inventoryItem.getId();

            for (String prefix : prefixes) {
                if (id.startsWith(prefix)) {
                    InventoryItem newItem = createCategoryItem(inventoryItem);
                    categoryItemList.add(newItem);
                    break;
                }
            }
        }

        return categoryItemList;
    }

    private static final Map<String, List<String>> CATEGORY_PREFIX_MAP = Map.of(
            "장비", Arrays.asList("1", "2"),
            "치장", Collections.singletonList("3"),
            "기타", Collections.singletonList("4")
    );

    private InventoryItem createCategoryItem(InventoryItem categoryItem) {
        return InventoryItem.builder()
                .id(categoryItem.getId())
                .name(categoryItem.getName())
                .quantity(categoryItem.getQuantity())
                .build();
    }

    public List<Button> buildButton() {
        List<Button> buttonList = new ArrayList<>();

        Button pageButton = Button.secondary("blank", "1/5").asDisabled();
        Button leftButton = Button.primary("inventory-left-0", "◄").asDisabled();
        Button rightButton = Button.primary("inventory-right-2", "►");
        Button cancelButton = Button.danger("cancel", "닫기");

        buttonList.add(leftButton.asDisabled());
        buttonList.add(pageButton);
        buttonList.add(rightButton);
        buttonList.add(cancelButton);

        return buttonList;
    }
}