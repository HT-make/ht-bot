package com.htmake.htbot.discord.commands.skill.data;

import com.htmake.htbot.domain.skill.presentation.data.response.SkillResponse;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SkillList {

    List<SkillResponse> firstSkillList;

    List<SkillResponse> secondSkillList;

    List<SkillResponse> thirdSkillList;
}
