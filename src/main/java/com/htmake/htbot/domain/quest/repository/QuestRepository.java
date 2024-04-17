package com.htmake.htbot.domain.quest.repository;

import com.htmake.htbot.domain.quest.entity.Quest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface QuestRepository extends JpaRepository<Quest, String> {
    Optional<Quest> findByPlayerId(String playerId);
}
