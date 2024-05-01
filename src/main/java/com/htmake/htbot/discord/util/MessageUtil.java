package com.htmake.htbot.discord.util;

import com.htmake.htbot.discord.commands.global.cache.MessageCache;
import com.htmake.htbot.global.cache.CacheFactory;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;

public class MessageUtil {

    private final MessageCache messageCache;

    public MessageUtil() {
        this.messageCache = CacheFactory.messageCache;
    }

    public void put(String playerId) {
        messageCache.put(playerId, true);
    }

    public void remove(String playerId) {
        messageCache.remove(playerId);
    }

    public boolean validCheck(Message message, String name) {
        MessageEmbed embed = message.getEmbeds().get(0);
        return !embed.getAuthor().getName().equals(name);
    }

    public boolean validCheck(String playerId) {
        return messageCache.containsKey(playerId);
    }
}
