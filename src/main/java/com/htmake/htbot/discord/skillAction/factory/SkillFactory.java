package com.htmake.htbot.discord.skillAction.factory;

import com.htmake.htbot.discord.skillAction.BasicSkill;
import com.htmake.htbot.discord.skillAction.skills.warrior.class1.*;
import com.htmake.htbot.discord.skillAction.skills.warrior.class2.*;
import com.htmake.htbot.discord.skillAction.skills.warrior.class3.*;
import com.htmake.htbot.discord.skillAction.skills.warrior.class4.*;
import com.htmake.htbot.discord.skillAction.skills.archer.class1.*;
import com.htmake.htbot.discord.skillAction.skills.archer.class2.*;
import com.htmake.htbot.discord.skillAction.skills.archer.class3.*;
import com.htmake.htbot.discord.skillAction.skills.archer.class4.*;
import com.htmake.htbot.discord.skillAction.skills.wizard.class1.*;
import com.htmake.htbot.discord.skillAction.skills.wizard.class2.*;
import com.htmake.htbot.discord.skillAction.skills.wizard.class3.*;
import com.htmake.htbot.discord.skillAction.skills.wizard.class4.*;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class SkillFactory {

    public static Map<String, BasicSkill> skillMap;

    public SkillFactory() {
        skillMap = new HashMap<>();

        //WARRIOR-Class1
        skillMap.put("S1101", new BasicSkill(new S1101()));
        skillMap.put("S1102", new BasicSkill(new S1102()));
        skillMap.put("S1103", new BasicSkill(new S1103()));
        skillMap.put("S1104", new BasicSkill(new S1104()));
        skillMap.put("S1105", new BasicSkill(new S1105()));
        //WARRIOR-Class2
        skillMap.put("S1201", new BasicSkill(new S1201()));
        skillMap.put("S1202", new BasicSkill(new S1202()));
        skillMap.put("S1203", new BasicSkill(new S1203()));
        skillMap.put("S1204", new BasicSkill(new S1204()));
        skillMap.put("S1205", new BasicSkill(new S1205()));
        skillMap.put("S1206", new BasicSkill(new S1206()));
        //WARRIOR-Class3
        skillMap.put("S1301", new BasicSkill(new S1301()));
        skillMap.put("S1302", new BasicSkill(new S1302()));
        skillMap.put("S1303", new BasicSkill(new S1303()));
        skillMap.put("S1304", new BasicSkill(new S1304()));
        skillMap.put("S1305", new BasicSkill(new S1305()));
        //WARRIOR-Class4
        skillMap.put("S1401", new BasicSkill(new S1401()));
        skillMap.put("S1402", new BasicSkill(new S1402()));
        skillMap.put("S1403", new BasicSkill(new S1403()));
        skillMap.put("S1404", new BasicSkill(new S1404()));
        skillMap.put("S1405", new BasicSkill(new S1405()));

        //ARCHER-Class1
        skillMap.put("S2101", new BasicSkill(new S2101()));
        skillMap.put("S2102", new BasicSkill(new S2102()));
        skillMap.put("S2103", new BasicSkill(new S2103()));
        skillMap.put("S2104", new BasicSkill(new S2104()));
        //ARCHER-Class2
        skillMap.put("S2201", new BasicSkill(new S2201()));
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

        //WIZARD-Class1
        skillMap.put("S3101", new BasicSkill(new S3101()));
        skillMap.put("S3102", new BasicSkill(new S3102()));
        skillMap.put("S3103", new BasicSkill(new S3103()));
        skillMap.put("S3104", new BasicSkill(new S3104()));
        //WIZARD-Class2
        skillMap.put("S3201", new BasicSkill(new S3201()));
        skillMap.put("S3202", new BasicSkill(new S3202()));
        skillMap.put("S3203", new BasicSkill(new S3203()));
        skillMap.put("S3204", new BasicSkill(new S3204()));
        skillMap.put("S3205", new BasicSkill(new S3205()));
        //WIZARD-Class3
        skillMap.put("S3301", new BasicSkill(new S3301()));
        skillMap.put("S3302", new BasicSkill(new S3302()));
        skillMap.put("S3303", new BasicSkill(new S3303()));
        skillMap.put("S3304", new BasicSkill(new S3304()));
        skillMap.put("S3305", new BasicSkill(new S3305()));
        skillMap.put("S3306", new BasicSkill(new S3306()));
        //WIZARD-Class4
        skillMap.put("S3401", new BasicSkill(new S3401()));
        skillMap.put("S3402", new BasicSkill(new S3402()));
        skillMap.put("S3403", new BasicSkill(new S3403()));
        skillMap.put("S3404", new BasicSkill(new S3404()));
    }
}
