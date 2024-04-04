package com.htmake.htbot.domain.inventory.service;

import com.htmake.htbot.domain.inventory.presentation.data.response.InventoryInfoListResponse;
public interface InventoryInfoService {
    InventoryInfoListResponse execute(String playerId);
}
