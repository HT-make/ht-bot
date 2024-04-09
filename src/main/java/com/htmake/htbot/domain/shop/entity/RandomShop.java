package com.htmake.htbot.domain.shop.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.json.JSONObject;

import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RandomShop {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "random_shop_id")
    private Long id;

    @OneToMany(mappedBy = "randomShop", cascade = CascadeType.ALL)
    private List<RandomShopWeapon> randomShopWeapons;

    @OneToMany(mappedBy = "randomShop", cascade = CascadeType.ALL)
    private List<RandomShopArmor> randomShopArmors;
}
