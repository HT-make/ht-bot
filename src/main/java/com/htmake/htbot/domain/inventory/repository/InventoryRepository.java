package com.htmake.htbot.domain.inventory.repository;

import com.htmake.htbot.domain.inventory.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InventoryRepository extends JpaRepository<Inventory, String> {
    List<Inventory> findByPlayerId(String playerId);

    Optional<Inventory> findByPlayerIdAndItemId(String playerId, String itemId);
}
