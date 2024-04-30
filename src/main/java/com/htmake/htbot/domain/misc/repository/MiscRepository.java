package com.htmake.htbot.domain.misc.repository;

import com.htmake.htbot.domain.misc.entity.Misc;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MiscRepository extends JpaRepository<Misc, String> {
    Optional<Misc> findByName(String name);
}
