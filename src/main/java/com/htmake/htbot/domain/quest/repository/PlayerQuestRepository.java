package com.htmake.htbot.domain.quest.repository;

import com.htmake.htbot.domain.quest.entity.PlayerQuest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PlayerQuestRepository extends JpaRepository<PlayerQuest, String> {
    Optional<PlayerQuest> findByPlayerId(String playerId);
}
