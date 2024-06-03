package com.htmake.htbot.domain.shop.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BossShop {
    @Id
    @Column(name = "boss_shop_item_id")
    private String id;

    @Column(name = "boss_shop_item_name", nullable = false)
    private String name;

    @Column(name = "boss_shop_item_coin", nullable = false)
    private int coin;
}
