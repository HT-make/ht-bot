package com.htmake.htbot.domain.player.repository;

import com.htmake.htbot.domain.player.entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StatusRepository extends JpaRepository<Status, Long> {

    Optional<Status> findById(String id);
}
