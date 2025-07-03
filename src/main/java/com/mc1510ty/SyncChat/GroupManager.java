package com.mc1510ty.SyncChat;

import java.util.List;

public class GroupManager {

    private final List<String> groups;

    public GroupManager(List<String> groups) {
        this.groups = groups;
    }

    public List<String> getGroups() {
        return groups;
    }

    public boolean isInGroup(String group) {
        return groups.contains(group);
    }
}