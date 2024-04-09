package com.htmake.htbot.domain.shop.presentation.data.response;

import com.htmake.htbot.domain.shop.entity.RandomShop;
import com.htmake.htbot.domain.shop.entity.RandomShopWeapon;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RandomShopWeaponResponse {

    private String id;

    private String name;

    private int gold;

    private int quantity;

    public static RandomShopWeaponResponse toResponse(RandomShopWeapon randomShopWeapon) {
        return RandomShopWeaponResponse.builder()
                .id(randomShopWeapon.getId())
                .name(randomShopWeapon.getName())
                .gold(randomShopWeapon.getGold())
                .quantity(randomShopWeapon.getQuantity())
                .build();
    }
}
