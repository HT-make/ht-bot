package com.htmake.htbot.discord.commands.skill.event;

import com.htmake.htbot.discord.commands.skill.cache.SkillCache;
import com.htmake.htbot.discord.commands.skill.data.SkillList;
import com.htmake.htbot.discord.commands.skill.util.SkillEventUtil;
import com.htmake.htbot.discord.util.ErrorUtil;
import com.htmake.htbot.global.cache.CacheFactory;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.util.List;

public class SkillListButtonEvent {

    private final ErrorUtil errorUtil;
    private final SkillEventUtil skillEventUtil;

    private final SkillCache skillCache;

    public SkillListButtonEvent() {
        this.errorUtil = new ErrorUtil();
        this.skillEventUtil = new SkillEventUtil();

        this.skillCache = CacheFactory.skillCache;
    }

    public void execute(ButtonInteractionEvent event, int page) {
        User user = event.getUser();

        if (!skillCache.containsKey(user.getId())){
            errorUtil.sendError(event.getHook(), "스킬", "스킬 목록을 찾을 수 없습니다.");
            return;
        }

        SkillList skillLists = skillCache.get(user.getId());
        MessageEmbed embed = null;
        if (page == 1){
            embed = skillEventUtil.skillListBuildEmbed(skillLists.getFirstSkillList(), event.getUser());
        } else if (page == 2){
            embed = skillEventUtil.skillListBuildEmbed(skillLists.getSecondSkillList(), event.getUser());
        } else if (page == 3){
            embed = skillEventUtil.skillListBuildEmbed(skillLists.getThirdSkillList(), event.getUser());
        }

        List<Button> buttonList = skillEventUtil.skillButtonEmbed(page, "", "skill-list");

        event.getHook().editOriginalEmbeds(embed)
                .setActionRow(buttonList)
                .queue();
    }
}
