package com.htmake.htbot.domain.shop.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RandomShopArmor {

    @Id
    @Column(name = "armor_id")
    private String id;

    @Column(name = "armor_name", nullable = false)
    private String name;

    @Column(name = "armor_gold", nullable = false)
    private int gold;

    @Column(name = "armor_quantity", nullable = false)
    private int quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "random_shop_id")
    private RandomShop randomShop;
}
