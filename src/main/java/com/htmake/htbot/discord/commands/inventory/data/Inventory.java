package com.htmake.htbot.discord.commands.inventory.data;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Inventory {

    private int gold;

    private int gem;

    private String category;

    private List<InventoryItem> inventoryItemList;

    private List<InventoryItem> categoryItemList;
}
