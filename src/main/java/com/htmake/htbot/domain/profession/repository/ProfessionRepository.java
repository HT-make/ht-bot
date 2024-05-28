package com.htmake.htbot.domain.profession.repository;

import com.htmake.htbot.domain.player.enums.Job;
import com.htmake.htbot.domain.profession.entity.Profession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProfessionRepository extends JpaRepository<Profession, Long> {

    Optional<Profession> findByNextJob(Job playerJob);

    List<Profession> findByPreviousJob(Job playerJob);
}
