package com.htmake.htbot.domain.player.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Equipment {

    @Id
    @Column(name = "player_id")
    private String id;

    @Column(name = "player_weapon_id", nullable = false)
    private String weaponId;

    @Column(name = "player_weapon_name", nullable = false)
    private String weaponName;

    @Column(name = "player_armor_id", nullable = false)
    private String armorId;

    @Column(name = "player_armor_name", nullable = false)
    private String armorName;
}
