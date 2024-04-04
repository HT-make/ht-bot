package com.htmake.htbot.domain.inventory.entity;

import com.htmake.htbot.domain.player.entity.Player;
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
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "inventory_id")
    private Long id;

    @Column(name = "item_id")
    private String itemId;

    @Column(name = "item_name", nullable = false)
    private String name;

    @Column(name = "item_quantity", nullable = false)
    private int quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id")
    private Player player;

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
