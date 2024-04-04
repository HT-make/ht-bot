package com.htmake.htbot.domain.inventory.presentation.data.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class InventoryInsertItemRequest {
    private String itemId;

    private String name;

    private int quantity;
}
