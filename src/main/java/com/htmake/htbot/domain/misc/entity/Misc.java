package com.htmake.htbot.domain.misc.entity;

import com.htmake.htbot.domain.quest.entity.MainQuest;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

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

    @OneToMany(mappedBy = "targetItem", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<MainQuest> mainQuest;
}
