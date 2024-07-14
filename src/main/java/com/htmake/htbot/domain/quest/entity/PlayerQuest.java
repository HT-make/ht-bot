package com.htmake.htbot.domain.quest.entity;

import com.htmake.htbot.domain.player.entity.Player;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PlayerQuest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "player_quest_id")
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "player_id")
    private Player player;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "main_quest_id")
    private MainQuest mainQuest;

    @Column(name = "read_dialogue")
    @ColumnDefault("false")
    private boolean readDialogue;

    public void setMainQuest(MainQuest mainQuest) {
        this.mainQuest = mainQuest;
    }

    public void setReadDialogue(boolean readDialogue) {
        this.readDialogue = readDialogue;
    }
}