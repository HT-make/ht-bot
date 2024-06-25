package com.htmake.htbot.discord.commands.battle.data.status.extend;

import com.htmake.htbot.discord.commands.battle.data.PlayerSkillStatus;
import com.htmake.htbot.discord.commands.battle.data.status.BasicStatus;
import com.htmake.htbot.domain.player.enums.Job;
import lombok.*;

import java.util.Map;

@Getter
@Setter
public class PlayerStatus extends BasicStatus {

    private int mana;

    private Job job;

    private Map<Integer, PlayerSkillStatus> playerSkill;

    public PlayerStatus(
            int level,
            String name,
            int damage,
            int health,
            int defence,
            int criticalChance,
            int criticalDamage,
            int mana,
            Job job,
            Map<Integer, PlayerSkillStatus> playerSkill
    ) {
        super(level, name, damage, health, defence, criticalChance, criticalDamage);
        this.mana = mana;
        this.job = job;
        this.playerSkill = playerSkill;
    }
}
