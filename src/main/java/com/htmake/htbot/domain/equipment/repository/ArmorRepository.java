package com.htmake.htbot.domain.equipment.repository;

import com.htmake.htbot.domain.equipment.entity.Armor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ArmorRepository extends JpaRepository<Armor, String> {
    Optional<Armor> findByName(String name);
}
