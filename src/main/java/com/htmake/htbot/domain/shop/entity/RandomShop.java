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
public class RandomShop {

    @Id
    @Column(name = "random_shop_item_id")
    private String id;

    @Column(name = "random_shop_item_name", nullable = false)
    private String name;

    @Column(name = "random_shop_item_gold", nullable = false)
    private int gold;

    @Column(name = "random_shop_item_quantity", nullable = false)
    private int quantity;

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
