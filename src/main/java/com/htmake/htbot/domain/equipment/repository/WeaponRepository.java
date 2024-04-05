package com.htmake.htbot.domain.equipment.repository;

import com.htmake.htbot.domain.equipment.entity.Weapon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WeaponRepository extends JpaRepository<Weapon, String> {
}
