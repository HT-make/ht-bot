package com.htmake.htbot.domain.player.repository;

import com.htmake.htbot.domain.player.entity.Player;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PlayerRepository extends JpaRepository<Player, Long> {

    Boolean existsById(String id);

    Optional<Player> findById(String id);
}
