package com.htmake.htbot.domain.player.repository;

import com.htmake.htbot.domain.player.entity.Equipment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EquipmentRepository extends JpaRepository<Equipment, String> {
}
