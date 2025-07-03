package com.mc1510ty.SyncChat;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;


public class SyncChat extends JavaPlugin {

    private GroupManager groupManager;
    private RedisManager redisManager;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        FileConfiguration config = getConfig();

        this.groupManager = new GroupManager(config.getStringList("groups"));

        String host = config.getString("redis.host", "localhost");
        int port = config.getInt("redis.port", 6379);

        this.redisManager = new RedisManager(this, host, port, groupManager.getGroups());
        redisManager.startSubscriber();

        getServer().getPluginManager().registerEvents(new ChatListener(this), this);

        getLogger().info("ChatBridge enabled. Groups: " + groupManager.getGroups());
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

