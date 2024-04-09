package com.htmake.htbot.domain.dungeon.repository;

import com.htmake.htbot.domain.dungeon.entity.Monster;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MonsterRepository extends JpaRepository<Monster, String> {
}
