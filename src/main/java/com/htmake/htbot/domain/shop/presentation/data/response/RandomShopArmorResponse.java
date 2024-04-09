package com.htmake.htbot.domain.shop.presentation.data.response;


import com.htmake.htbot.domain.shop.entity.RandomShopArmor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RandomShopArmorResponse {

    private String id;

    private String name;

    private int gold;

    private int quantity;

    public static RandomShopArmorResponse toResponse(RandomShopArmor randomShopArmor) {
        return RandomShopArmorResponse.builder()
                .id(randomShopArmor.getId())
                .name(randomShopArmor.getName())
                .gold(randomShopArmor.getGold())
                .quantity(randomShopArmor.getQuantity())
                .build();
    }
}
