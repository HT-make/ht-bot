package com.htmake.htbot.discord.util;

import org.apache.commons.math3.random.MersenneTwister;
import org.apache.commons.math3.random.RandomGenerator;

public class RandomUtil {

    public static boolean randomPercentage(int value) {
        RandomGenerator random = new MersenneTwister();
        random.setSeed(System.currentTimeMillis() ^ System.nanoTime());

        int randomNum = random.nextInt(100);

        return randomNum < value;
    }
}
