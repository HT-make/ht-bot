package com.htmake.htbot.domain.quest.entity.target.misc;

import com.htmake.htbot.domain.misc.entity.Misc;
import com.htmake.htbot.domain.quest.entity.MainQuest;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TargetMisc {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "target_misc_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "misc_id")
    private Misc misc;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "main_quest_id")
    private MainQuest mainQuest;

    @Column(name = "required_misc_quantity", nullable = false)
    private int requiredQuantity;
}
