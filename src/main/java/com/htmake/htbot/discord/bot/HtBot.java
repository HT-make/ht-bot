package com.htmake.htbot.discord.bot;

import com.htmake.htbot.discord.commands.InventoryCommand;
import com.htmake.htbot.discord.commands.battle.BattleCommand;
import com.htmake.htbot.discord.commands.DungeonCommand;
import com.htmake.htbot.discord.commands.player.PlayerCommand;
import com.htmake.htbot.discord.commands.GlobalCommand;
import com.htmake.htbot.discord.listeners.EventListener;
import com.htmake.htbot.unirest.HttpClient;
import io.github.cdimascio.dotenv.Dotenv;
import lombok.Getter;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;

import javax.security.auth.login.LoginException;

@Getter
public class HtBot {

    private final HttpClient httpClient;
    private final Dotenv config;
    private final ShardManager shardManager;

    public HtBot(HttpClient httpClient) throws LoginException {
        this.httpClient = httpClient;

        // Load environment variables
        config = Dotenv.configure().load();
        String token = config.get("TOKEN");

        // Build shard manager
        DefaultShardManagerBuilder builder = DefaultShardManagerBuilder.createDefault(token);
        builder.setStatus(OnlineStatus.ONLINE);
        builder.setActivity(Activity.listening("이야기"));
        builder.enableIntents(GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_MEMBERS, GatewayIntent.MESSAGE_CONTENT);
        builder.setMemberCachePolicy(MemberCachePolicy.ALL);
        builder.setChunkingFilter(ChunkingFilter.ALL);
        shardManager = builder.build();

        // Register listeners
        shardManager.addEventListener(
                new EventListener(),
                new GlobalCommand(),
                new PlayerCommand(this.httpClient),
                new DungeonCommand(this.httpClient),
                new BattleCommand(this.httpClient),
                new InventoryCommand(this.httpClient)
        );
    }
}
