package com.htmake.htbot.domain.inventory.presentation.data.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InventoryInfoListResponse {
    List<InventoryInfoResponse> inventoryList;
}
