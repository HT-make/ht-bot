package com.htmake.htbot.domain.quest.entity.dialogue;

import com.htmake.htbot.domain.quest.entity.MainQuest;
import com.htmake.htbot.domain.quest.enums.Trigger;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class QuestDialogue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "quest_dialogue_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "main_quest_id", nullable = false)
    private MainQuest mainQuest;

    @Column(name = "dialogue_character", nullable = false)
    private String character;

    @Column(name = "dialogue_text", nullable = false)
    private String dialogue;

    @Column(name = "dialogue_sequence", nullable = false)
    private int sequence;

    @Enumerated(EnumType.STRING)
    @Column(name = "dialogue_trigger", nullable = false)
    private Trigger trigger;
}
