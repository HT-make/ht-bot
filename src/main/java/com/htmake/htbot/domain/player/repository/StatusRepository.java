package com.htmake.htbot.domain.player.repository;

import com.htmake.htbot.domain.player.entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatusRepository extends JpaRepository<Status, String> {
}
