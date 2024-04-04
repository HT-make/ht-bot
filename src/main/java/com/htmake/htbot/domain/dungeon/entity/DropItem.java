package com.htmake.htbot.domain.dungeon.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DropItem {

    @Id
    @Column(name = "drop_item_id")
    private String id;

    @Column(name = "drop_item_name", nullable = false)
    private String name;

    @Column(name = "drop_item_chance", nullable = false)
    private int getChance;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "monster_id")
    private Monster monster;
}
