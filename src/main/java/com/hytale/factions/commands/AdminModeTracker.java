package com.hytale.factions.commands;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class AdminModeTracker {
    private final Set<UUID> adminModePlayers = new HashSet<>();

    public boolean toggle(UUID playerId) {
        if (adminModePlayers.contains(playerId)) {
            adminModePlayers.remove(playerId);
            return false;
        }
        adminModePlayers.add(playerId);
        return true;
    }

    public boolean isAdmin(UUID playerId) {
        return adminModePlayers.contains(playerId);
    }
}
