package com.htmake.htbot.domain.dungeon.repository;

import com.htmake.htbot.domain.dungeon.entity.Dungeon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DungeonRepository extends JpaRepository<Dungeon, String> {
}
