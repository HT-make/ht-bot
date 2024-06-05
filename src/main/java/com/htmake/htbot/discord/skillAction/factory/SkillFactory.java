package com.htmake.htbot.discord.skillAction.factory;

import com.htmake.htbot.discord.skillAction.BasicSkill;
import com.htmake.htbot.discord.skillAction.skills.archer.class1.*;
import com.htmake.htbot.discord.skillAction.skills.archer.class2.*;
import com.htmake.htbot.discord.skillAction.skills.archer.class3.*;
import com.htmake.htbot.discord.skillAction.skills.archer.class4.*;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class SkillFactory {

    public static Map<String, BasicSkill> skillMap;

    public SkillFactory() {
        skillMap = new HashMap<>();

        //ARCHER-Class1
        skillMap.put("S2101", new BasicSkill(new S2101()));
        skillMap.put("S2102", new BasicSkill(new S2102()));
        skillMap.put("S2103", new BasicSkill(new S2103()));
        skillMap.put("S2104", new BasicSkill(new S2104()));
        skillMap.put("S2201", new BasicSkill(new S2201()));
        //ARCHER-Class2
        skillMap.put("S2202", new BasicSkill(new S2202()));
        skillMap.put("S2203", new BasicSkill(new S2203()));
        skillMap.put("S2204", new BasicSkill(new S2204()));
        skillMap.put("S2205", new BasicSkill(new S2205()));
        //ARCHER-Class3
        skillMap.put("S2301", new BasicSkill(new S2301()));
        skillMap.put("S2302", new BasicSkill(new S2302()));
        skillMap.put("S2303", new BasicSkill(new S2303()));
        skillMap.put("S2304", new BasicSkill(new S2304()));
        skillMap.put("S2305", new BasicSkill(new S2305()));
        //ARCHER-Class4
        skillMap.put("S2401", new BasicSkill(new S2401()));
        skillMap.put("S2402", new BasicSkill(new S2402()));
        skillMap.put("S2403", new BasicSkill(new S2403()));
        skillMap.put("S2404", new BasicSkill(new S2404()));
        skillMap.put("S2405", new BasicSkill(new S2405()));
    }
}
