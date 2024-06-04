package com.htmake.htbot.discord.skillAction.factory;

import com.htmake.htbot.discord.skillAction.BasicSkill;
import com.htmake.htbot.discord.skillAction.skills.archer.class1.S2101;
import com.htmake.htbot.discord.skillAction.skills.archer.class1.S2102;
import com.htmake.htbot.discord.skillAction.skills.archer.class1.S2103;
import com.htmake.htbot.discord.skillAction.skills.archer.class1.S2104;
import com.htmake.htbot.discord.skillAction.skills.archer.class2.S2202;
import com.htmake.htbot.discord.skillAction.skills.archer.class2.S2205;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class SkillFactory {

    public static Map<String, BasicSkill> skillMap;

    public SkillFactory() {
        skillMap = new HashMap<>();

        //ARCHER
        skillMap.put("S2101", new BasicSkill(new S2101()));
        skillMap.put("S2102", new BasicSkill(new S2102()));
        skillMap.put("S2103", new BasicSkill(new S2103()));
        skillMap.put("S2104", new BasicSkill(new S2104()));
        skillMap.put("S2202", new BasicSkill(new S2202()));
        skillMap.put("S2205", new BasicSkill(new S2205()));
    }
}
