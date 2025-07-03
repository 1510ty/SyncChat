package com.mc1510ty.SyncChat;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener {

    private final SyncChat plugin;

    public ChatListener(SyncChat plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        String player = event.getPlayer().getName();
        String message = event.getMessage();

        String fullMessage = "<" + player + "> " + message;

        for (String group : plugin.getGroupManager().getGroups()) {
            plugin.getRedisManager().publish(group, fullMessage);
        }
    }
}
