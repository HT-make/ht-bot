package com.htmake.htbot.discord.skillAction.factory;

import com.htmake.htbot.discord.skillAction.BasicSkill;
import com.htmake.htbot.discord.skillAction.skills.archer.*;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class SkillFactory {

    public static Map<String, BasicSkill> skillMap;

    public SkillFactory() {
        skillMap = new HashMap<>();

        //ARCHER
        skillMap.put("S2101", new BasicSkill(new QuickFiring()));
        skillMap.put("S2102", new BasicSkill(new ArrowRain()));
        skillMap.put("S2103", new BasicSkill(new HitI()));
        skillMap.put("S2104", new BasicSkill(new Breathing()));
        skillMap.put("S2202", new BasicSkill(new BoomShot()));
        skillMap.put("S2205", new BasicSkill(new FocusI()));
    }
}
