package com.hytale.factions.models;

import java.util.UUID;

public class Claim {
    private final UUID factionId;
    private final String worldId;
    private final int minX;
    private final int minY;
    private final int minZ;
    private final int maxX;
    private final int maxY;
    private final int maxZ;
    private final ClaimType type;

    public Claim(UUID factionId, String worldId, int minX, int minY, int minZ, int maxX, int maxY, int maxZ, ClaimType type) {
        this.factionId = factionId;
        this.worldId = worldId;
        this.minX = minX;
        this.minY = minY;
        this.minZ = minZ;
        this.maxX = maxX;
        this.maxY = maxY;
        this.maxZ = maxZ;
        this.type = type;
    }

    public UUID getFactionId() {
        return factionId;
    }

    public String getWorldId() {
        return worldId;
    }

    public int getMinX() {
        return minX;
    }

    public int getMinY() {
        return minY;
    }

    public int getMinZ() {
        return minZ;
    }

    public int getMaxX() {
        return maxX;
    }

    public int getMaxY() {
        return maxY;
    }

    public int getMaxZ() {
        return maxZ;
    }

    public ClaimType getType() {
        return type;
    }
}
