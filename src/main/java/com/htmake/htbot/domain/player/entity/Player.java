package com.htmake.htbot.domain.player.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Player {

    @Id
    @Column(name = "player_id")
    private String id;

    @Column(name = "player_name", nullable = false)
    private String name;

    @Column(name = "player_level", nullable = false)
    private int level;

    @Column(name = "player_current_exp", nullable = false)
    private int currentExp;

    @Column(name = "player_max_exp", nullable = false)
    private int maxExp;
}
