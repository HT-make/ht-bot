package com.htmake.htbot.domain.profession.entity;

import com.htmake.htbot.domain.player.enums.Job;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Profession {
    @Id
    @Column(name = "profession_id")
    private Long id;

    @Column(name = "profession_level")
    private int level;

    @Column(name = "profession_gold")
    private int gold;

    @Column(name = "profession_gem")
    private int gem;

    @Column(name = "previous_job")
    @Enumerated(EnumType.STRING)
    private Job previousJob;

    @Column(name = "next_job")
    @Enumerated(EnumType.STRING)
    private Job nextJob;

    @Column(name = "profession_item_name")
    private String itemName;

    @Column(name = "profession_item_quantity")
    private int itemQuantity;

    @Column(name = "profession_description")
    private String description;
}
