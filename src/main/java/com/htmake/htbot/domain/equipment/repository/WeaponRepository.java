package com.htmake.htbot.domain.equipment.repository;

import com.htmake.htbot.domain.equipment.entity.Weapon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WeaponRepository extends JpaRepository<Weapon, String> {

    Optional<Weapon> findByName(String name);

    boolean existsByName(String name);
}
