package com.htmake.htbot.domain.quest.entity.target.monster;

import com.htmake.htbot.domain.player.entity.Player;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PlayerTargetMonster {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "player_target_monster_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id")
    private Player player;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_monster_id")
    private TargetMonster targetMonster;

    @Column(name = "current_monster_quantity", nullable = false)
    private int currentQuantity;

    public void addCurrentQuantity() {
        this.currentQuantity = Math.min(targetMonster.getRequiredQuantity(), this.currentQuantity + 1);
    }
}
