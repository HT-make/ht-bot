package com.htmake.htbot.domain.shop.presentation.data.temp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RandomShopItemTemp {

    private String id;

    private String name;

    private int gold;

    private int quantity;
}
