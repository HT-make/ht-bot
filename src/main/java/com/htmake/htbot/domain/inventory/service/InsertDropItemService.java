package com.htmake.htbot.domain.inventory.service;

import com.htmake.htbot.domain.inventory.presentation.data.request.InsertDropItemListRequest;

public interface InsertDropItemService {

    void execute(String playerId, InsertDropItemListRequest request);
}
