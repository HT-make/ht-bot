package com.htmake.htbot.domain.equipment.enums;

import com.htmake.htbot.domain.player.enums.Job;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum WeaponType {
    SWORD(Job.WARRIOR),
    ARROW(Job.ARCHER),
    STAFF(Job.WIZARD);

    private final Job job;
}
