package com.htmake.htbot.domain.shop.presentation.data.response;

import com.htmake.htbot.domain.shop.entity.BossShop;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BossShopItemResponse {

    private String id;

    private String name;

    private int coin;

    public static BossShopItemResponse toResponse(BossShop bossShop) {
        return BossShopItemResponse.builder()
                .id(bossShop.getId())
                .name(bossShop.getName())
                .coin(bossShop.getCoin())
                .build();
    }
}
