package com.mc1510ty.SyncChat;

import org.bukkit.Bukkit;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

import java.util.List;

public class RedisManager {

    private final SyncChat plugin;
    private final Jedis jedis;
    private final List<String> groups;

    public RedisManager(SyncChat plugin, String host, int port, List<String> groups) {
        this.plugin = plugin;
        this.jedis = new Jedis(host, port);
        this.groups = groups;
    }

    public void startSubscriber() {
        new Thread(() -> {
            try {
                jedis.subscribe(new JedisPubSub() {
                    @Override
                    public void onMessage(String channel, String message) {
                        plugin.getLogger().info("[Redis] " + channel + ": " + message);

                        Bukkit.getScheduler().runTask(plugin, () -> {
                            Bukkit.broadcastMessage("[" + channel.replace("chat_group:", "") + "] " + message);
                        });
                    }
                }, groups.stream().map(group -> "chat_group:" + group).toArray(String[]::new));
            } catch (Exception e) {
                plugin.getLogger().warning("Redis subscriber stopped: " + e.getMessage());
            }
        }, "Redis-Subscriber").start();
    }

    public void publish(String group, String message) {
        jedis.publish("chat_group:" + group, message);
    }

    public void close() {
        jedis.close();
    }
}

