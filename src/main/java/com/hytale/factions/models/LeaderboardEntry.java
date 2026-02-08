package com.hytale.factions.models;

import java.util.UUID;

public class LeaderboardEntry {
    private final UUID factionId;
    private final String factionName;
    private final long score;

    public LeaderboardEntry(UUID factionId, String factionName, long score) {
        this.factionId = factionId;
        this.factionName = factionName;
        this.score = score;
    }

    public UUID getFactionId() {
        return factionId;
    }

    public String getFactionName() {
        return factionName;
    }

    public long getScore() {
        return score;
    }
}
