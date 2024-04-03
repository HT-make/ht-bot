package com.htmake.htbot.domain.dungeon.repository;

import com.htmake.htbot.domain.dungeon.entity.Monster;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MonsterRepository extends JpaRepository<Monster, Long> {

    Optional<Monster> findByName(String name);
}
