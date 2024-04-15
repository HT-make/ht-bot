package com.htmake.htbot.domain.shop.service;

import com.htmake.htbot.domain.shop.presentation.data.request.RandomShopPurchaseRequest;
import com.htmake.htbot.domain.shop.presentation.data.response.SuccessPurchaseResponse;

public interface RandomShopItemPurchaseService {

    SuccessPurchaseResponse execute(String playerId, RandomShopPurchaseRequest request);
}
