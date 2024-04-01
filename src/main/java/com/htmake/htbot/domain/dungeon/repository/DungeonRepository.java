package com.htmake.htbot.domain.dungeon.repository;

import com.htmake.htbot.domain.dungeon.entity.Dungeon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DungeonRepository extends JpaRepository<Dungeon, Long> {

    Optional<Dungeon> findById(String id);
}
