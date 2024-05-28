package com.htmake.htbot.domain.shop.service;

import com.htmake.htbot.domain.shop.presentation.data.request.ItemSellRequest;

public interface ItemSellService {
    void execute(String playerId, ItemSellRequest request);
}
