package com.htmake.htbot.discord.commands.inventory.data;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InventoryItem {

    private String id;

    private String name;

    private int quantity;
}
