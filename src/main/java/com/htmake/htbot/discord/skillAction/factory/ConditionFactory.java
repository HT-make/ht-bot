package com.htmake.htbot.discord.skillAction.factory;

import com.htmake.htbot.discord.skillAction.condition.Condition;
import com.htmake.htbot.discord.skillAction.condition.extend.etc.Faint;
import com.htmake.htbot.discord.skillAction.condition.extend.etc.Invincible;
import com.htmake.htbot.discord.skillAction.condition.extend.etc.Thorn;
import com.htmake.htbot.discord.skillAction.condition.extend.buff.extend.*;
import com.htmake.htbot.discord.skillAction.condition.extend.damageOverTime.extend.Bleeding;
import com.htmake.htbot.discord.skillAction.condition.extend.damageOverTime.extend.Fire;
import com.htmake.htbot.discord.skillAction.condition.extend.damageOverTime.extend.Poison;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ConditionFactory {

    public static Map<String, Condition> conditionMap;

    public ConditionFactory() {
        conditionMap = new HashMap<>();

        // BUFF
        conditionMap.put("absorption_1", new Absorption(1));
        conditionMap.put("absorption_2", new Absorption(2));

        conditionMap.put("blind_1", new Blind(1));
        conditionMap.put("blind_2", new Blind(2));

        conditionMap.put("curse_1", new Curse(1));
        conditionMap.put("curse_2", new Curse(2));

        conditionMap.put("frostbite_1", new Frostbite(1));
        conditionMap.put("frostbite_2", new Frostbite(2));

        conditionMap.put("hit_1", new Hit(1));
        conditionMap.put("hit_2", new Hit(2));
        conditionMap.put("hit_3", new Hit(3));

        conditionMap.put("power_1", new Power(1));
        conditionMap.put("power_2", new Power(2));
        conditionMap.put("power_3", new Power(3));

        conditionMap.put("protect_1", new Protect(1));
        conditionMap.put("protect_2", new Protect(2));

        // DOT
        conditionMap.put("bleeding_1", new Bleeding(1));
        conditionMap.put("bleeding_2", new Bleeding(2));

        conditionMap.put("fire_1", new Fire(1));
        conditionMap.put("fire_2", new Fire(2));
        conditionMap.put("fire_3", new Fire(3));

        conditionMap.put("poison_1", new Poison(1));
        conditionMap.put("poison_2", new Poison(2));

        // OTHERS
        conditionMap.put("faint_1", new Faint(1));
        conditionMap.put("faint_2", new Faint(2));

        conditionMap.put("invincible", new Invincible());

        conditionMap.put("thorn_1", new Thorn(1));
        conditionMap.put("thorn_2", new Thorn(2));
    }

    public static Condition getCondition(String id) {
        try {
            Condition condition = conditionMap.get(id);
            return (Condition) condition.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }
}
