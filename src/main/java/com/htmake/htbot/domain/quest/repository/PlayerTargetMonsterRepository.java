package com.htmake.htbot.domain.quest.repository;

import com.htmake.htbot.domain.quest.entity.target.monster.PlayerTargetMonster;
import com.htmake.htbot.domain.quest.entity.target.monster.TargetMonster;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PlayerTargetMonsterRepository extends JpaRepository<PlayerTargetMonster, Long> {

    Optional<PlayerTargetMonster> findByPlayerIdAndTargetMonster(String playerId, TargetMonster t);
}
