package com.htmake.htbot.discord.commands.skill.event;

import com.htmake.htbot.discord.commands.skill.cache.SkillCache;
import com.htmake.htbot.discord.commands.skill.data.SkillList;
import com.htmake.htbot.discord.commands.skill.util.SkillEventUtil;
import com.htmake.htbot.discord.util.ErrorUtil;
import com.htmake.htbot.domain.skill.presentation.data.response.SkillResponse;
import com.htmake.htbot.global.cache.CacheFactory;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class RegisterSkillPageButtonEvent {

    private final ErrorUtil errorUtil;
    private final SkillEventUtil skillEventUtil;

    private final SkillCache skillCache;

    public RegisterSkillPageButtonEvent() {
        this.errorUtil = new ErrorUtil();
        this.skillEventUtil = new SkillEventUtil();

        this.skillCache = CacheFactory.skillCache;
    }

    public void execute(ButtonInteractionEvent event, int page, String number) {
        User user = event.getUser();

        if (!skillCache.containsKey(user.getId())) {
            errorUtil.sendError(event.getHook(), "스킬", "스킬 목록을 찾을 수 없습니다.");
            return;
        }

        SkillList skillLists = skillCache.get(user.getId());

        List<ActionRow> actionRowList = new ArrayList<>();
        List<Button> buttonList = skillEventUtil.skillButtonEmbed(page, number, "skill-enroll");

        List<SkillResponse> selectedSkillList = null;

        MessageEmbed embed;

        switch (page) {
            case 1 -> selectedSkillList = skillLists.getFirstSkillList();
            case 2 -> selectedSkillList = skillLists.getSecondSkillList();
            case 3 -> selectedSkillList = skillLists.getThirdSkillList();
        }

        if (selectedSkillList != null && !selectedSkillList.isEmpty()) {
            embed = skillEventUtil.skillListBuildEmbed(selectedSkillList, event.getUser());
            StringSelectMenu menu = buildMenu(selectedSkillList, number);
            actionRowList.add(ActionRow.of(menu));
        } else {
            embed = new EmbedBuilder()
                    .setColor(Color.GREEN)
                    .setAuthor(event.getUser().getName(), null, event.getUser().getAvatarUrl())
                    .setTitle("스킬 등록")
                    .setDescription("등록할 수 있는 스킬이 없습니다.")
                    .build();
        }

        actionRowList.add(ActionRow.of(buttonList));

        event.getHook().editOriginalEmbeds(embed)
                .setComponents(actionRowList)
                .queue();
    }

    private StringSelectMenu buildMenu(List<SkillResponse> skillList, String number) {
        return StringSelectMenu.create("skillMenu")
                .addOptions(skillEventUtil.buildOptionList(skillList, number))
                .build();
    }
}
