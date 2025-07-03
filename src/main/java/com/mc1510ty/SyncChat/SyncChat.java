package com.mc1510ty.SyncChat;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SyncChat extends JavaPlugin {

    private GroupManager groupManager;
    private RedisManager redisManager;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        FileConfiguration config = getConfig();

        String host = config.getString("redis.host", "localhost");
        int port = config.getInt("redis.port", 6379);
        List<String> groups = config.getStringList("groups");

        this.groupManager = new GroupManager(groups);
        this.redisManager = new RedisManager(this, host, port, groups);

        redisManager.startSubscriber();
        getServer().getPluginManager().registerEvents(new ChatListener(this), this);

        getLogger().info("SyncChat enabled. Groups: " + groups);
    }

    @Override
    public void onDisable() {
        redisManager.close();
    }

    public GroupManager getGroupManager() {
        return groupManager;
    }

    public RedisManager getRedisManager() {
        return redisManager;
    }
}
