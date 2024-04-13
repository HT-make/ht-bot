package com.htmake.htbot.domain.monster.repository;

import com.htmake.htbot.domain.monster.entity.Monster;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MonsterRepository extends JpaRepository<Monster, String> {
}
