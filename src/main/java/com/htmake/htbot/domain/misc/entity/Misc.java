package com.htmake.htbot.domain.misc.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Misc {

    @Id
    @Column(name = "item_id")
    private String id;

    @Column(name = "item_name", nullable = false)
    private String name;

    @Column(name = "item_gold", nullable = false)
    private int gold;
}
