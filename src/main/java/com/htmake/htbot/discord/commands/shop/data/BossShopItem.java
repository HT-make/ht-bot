package com.htmake.htbot.discord.commands.shop.data;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BossShopItem {

    private String id;

    private String name;

    private int coin;
}
