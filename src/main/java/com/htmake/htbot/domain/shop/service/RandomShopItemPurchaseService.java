package com.htmake.htbot.domain.shop.service;

import com.htmake.htbot.domain.shop.presentation.data.request.RandomShopPurchaseRequest;

public interface RandomShopItemPurchaseService {

    void execute(String playerId, RandomShopPurchaseRequest request);
}
