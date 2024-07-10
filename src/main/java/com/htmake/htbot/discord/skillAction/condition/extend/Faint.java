package com.htmake.htbot.discord.skillAction.condition.extend;

import com.htmake.htbot.discord.skillAction.condition.Condition;
import com.htmake.htbot.discord.util.RandomUtil;

public class Faint extends Condition {

    private int value;

    public Faint(int version) {
        initialize("faint", "기절" + getRomanNumeral(version), ":dizzy:", 2);
        setValue(version);
    }

    private void setValue(int version) {
        switch (version) {
            case 1 -> this.value = 50;
            case 2 -> this.value = 75;
            case 3 -> this.value = 100;
        }
    }

    public boolean applyEffect() {
        return RandomUtil.randomPercentage(value);
    }
}
