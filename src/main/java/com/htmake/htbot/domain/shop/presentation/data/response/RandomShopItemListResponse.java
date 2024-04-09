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
    List<RandomShopWeaponResponse> randomShopWeaponList;
    List<RandomShopArmorResponse> randomShopArmorList;
}
