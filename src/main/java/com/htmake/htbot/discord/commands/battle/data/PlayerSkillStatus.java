package com.htmake.htbot.discord.commands.battle.data;

import com.htmake.htbot.discord.skillAction.BasicSkill;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PlayerSkillStatus {

    private BasicSkill basicSkill;

    private String name;

    private int mana;
}
