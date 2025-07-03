package com.mc1510ty.SyncChat;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class MessageCache {

    private static final Map<String, Set<String>> sentMessages = new HashMap<>();

    public static void addSentMessage(String group, String message) {
        sentMessages.computeIfAbsent(message, k -> new HashSet<>()).add(group);
    }

    public static boolean shouldProcess(String incomingGroup, String message, GroupManager groupManager) {
        Set<String> sentGroups = sentMessages.get(message);
        if (sentGroups == null) return true;

        // 送信したグループの中で一番優先度が高いものを探す
        String highestPriorityGroup = null;
        int highestPriority = Integer.MAX_VALUE;

        for (String sentGroup : sentGroups) {
            int priority = groupManager.getPriority(sentGroup);
            if (priority != -1 && priority < highestPriority) {
                highestPriority = priority;
                highestPriorityGroup = sentGroup;
            }
        }

        // 受信したグループの優先度を取得
        int incomingPriority = groupManager.getPriority(incomingGroup);

        // 受信したグループが優先度最高なら表示、それ以外は無視
        return incomingPriority <= highestPriority;
    }
}
