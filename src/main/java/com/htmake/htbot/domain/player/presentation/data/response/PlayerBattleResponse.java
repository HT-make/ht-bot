package com.htmake.htbot.domain.player.presentation.data.response;

import com.htmake.htbot.domain.player.enums.Job;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PlayerBattleResponse {

    private int level;

    private int damage;

    private int health;

    private int defence;

    private int mana;

    private int criticalChance;

    private int criticalDamage;

    private Job job;

    private List<PlayerSkillResponse> skillList;
}
