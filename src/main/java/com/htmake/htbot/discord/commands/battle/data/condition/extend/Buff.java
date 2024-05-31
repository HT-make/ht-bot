package com.htmake.htbot.discord.commands.battle.data.condition.extend;

import com.htmake.htbot.discord.commands.battle.data.condition.Condition;
import com.htmake.htbot.discord.commands.battle.data.status.BasicStatus;
import com.htmake.htbot.discord.skillAction.type.BuffStatus;
import com.htmake.htbot.discord.skillAction.type.BuffType;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class Buff extends Condition {

    private final Double value;
    private final BuffType buffType;
    private final BuffStatus target;

    public Buff(String id, String name, String emoji, int turn, Double value, BuffType buffType, BuffStatus target) {
        super(id, name, emoji, turn);
        this.value = value;
        this.buffType = buffType;
        this.target = target;
    }

    public void apply(BasicStatus status, BasicStatus originalStatus) {
        switch (buffType) {
            case UP -> applyBuff(status, originalStatus, true);
            case DOWN -> applyBuff(status, originalStatus, false);
        }
    }

    public void unapply(BasicStatus status, BasicStatus originalStatus) {
        switch (buffType) {
            case UP -> applyBuff(status, originalStatus, false);
            case DOWN -> applyBuff(status, originalStatus, true);
        }
    }

    private void applyBuff(BasicStatus status, BasicStatus originalStatus, boolean isBuff) {
        int multiplier = isBuff ? 1 : -1;
        switch (target) {
            case DAMAGE -> applyStatusChange(status::getDamage, status::setDamage, originalStatus.getDamage(), multiplier);
            case DEFENCE -> applyStatusChange(status::getDefence, status::setDefence, originalStatus.getDefence(), multiplier);
            case CRITICAL_CHANCE -> applyCriticalChanceChange(status, multiplier);
        }
    }

    private void applyStatusChange(Supplier<Integer> getter, Consumer<Integer> setter, int originalValue, int multiplier) {
        int currentValue = getter.get();
        int valueChange = (int) (originalValue * value * multiplier);
        setter.accept(Math.max(0, currentValue + valueChange));
    }

    private void applyCriticalChanceChange(BasicStatus status, int multiplier) {
        int criticalChance = status.getCriticalChance();
        int valueChange = (int) (value * 100 * multiplier);
        status.setCriticalChance(Math.max(0, criticalChance + valueChange));
    }
}
