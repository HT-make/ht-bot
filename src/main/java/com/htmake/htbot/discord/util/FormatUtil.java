package com.htmake.htbot.discord.util;

import java.text.DecimalFormat;

public class FormatUtil {

    private static final DecimalFormat formatter = new DecimalFormat("###,###,###");

    public static String decimalFormat(int value){
        return formatter.format(value);
    }

    public static String decimalFormat(String value) {
        return formatter.format(Integer.parseInt(value));
    }
}
