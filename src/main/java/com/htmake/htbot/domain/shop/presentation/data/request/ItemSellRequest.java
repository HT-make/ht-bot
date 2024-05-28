package com.htmake.htbot.domain.shop.presentation.data.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ItemSellRequest {

    private String category;

    private String name;

    private int quantity;
}
