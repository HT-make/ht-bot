package com.htmake.htbot.domain.quest.repository;

import com.htmake.htbot.domain.quest.entity.MainQuest;
import com.htmake.htbot.domain.quest.entity.dialogue.QuestDialogue;
import com.htmake.htbot.domain.quest.enums.Trigger;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestDialogueRepository extends JpaRepository<QuestDialogue, Long> {

    List<QuestDialogue> findByMainQuestAndTrigger(MainQuest m, Trigger t, Sort s);
}
