package com.htmake.htbot.domain.inventory.presentation.data.response;

import com.htmake.htbot.domain.inventory.entity.Inventory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InventoryInfoResponse {

    private String id;

    private String name;

    private int quantity;

    public static InventoryInfoResponse toResponse(Inventory inventory) {
        return InventoryInfoResponse.builder()
                .id(inventory.getItemId())
                .name(inventory.getName())
                .quantity(inventory.getQuantity())
                .build();
    }
}
