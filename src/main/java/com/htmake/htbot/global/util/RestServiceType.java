package com.htmake.htbot.global.util;

import io.github.cdimascio.dotenv.Dotenv;

public final class RestServiceType {

    private static final Dotenv config = Dotenv.load();
    static String defaultUrl = config.get("DEFAULT_URL");

    public static final String DEFAULT_URL = defaultUrl;
}
