package com.htmake.htbot.domain.shop.presentation.data.response;

import com.htmake.htbot.domain.shop.entity.RandomShop;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RandomShopItemResponse {

    private String id;

    private String name;

    private int gold;

    private int quantity;

    public static RandomShopItemResponse toResponse(RandomShop randomShop) {
        return RandomShopItemResponse.builder()
                .id(randomShop.getId())
                .name(randomShop.getName())
                .gold(randomShop.getGold())
                .quantity(randomShop.getQuantity())
                .build();
    }
}
