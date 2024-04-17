package com.htmake.htbot.domain.monster.repository;

import com.htmake.htbot.domain.monster.entity.Monster;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MonsterRepository extends JpaRepository<Monster, String> {
    Optional<Monster> findByName(String name);
}
