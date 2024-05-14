package com.htmake.htbot.domain.dungeon.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DungeonKey {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dungeon_key_id")
    private Long id;

    @Column(name = "dungeon_key_item_id")
    private String itemId;

    @Column(name = "dungeon_key_name")
    private String name;

    @Column(name = "dungeon_key_quantity")
    private int quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dungeon_id")
    private Dungeon dungeon;
}
