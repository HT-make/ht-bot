package com.htmake.htbot.domain.shop.presentation.data.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RandomShopItemListResponse {
    private List<RandomShopWeaponResponse> randomShopWeaponList;
    private List<RandomShopArmorResponse> randomShopArmorList;
}
