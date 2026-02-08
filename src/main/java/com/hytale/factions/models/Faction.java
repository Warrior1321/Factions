package com.hytale.factions.models;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Faction {
    private final UUID id;
    private String name;
    private UUID leaderId;
    private final Instant createdAt;
    private final List<Claim> claims = new ArrayList<>();

    public Faction(UUID id, String name, UUID leaderId, Instant createdAt) {
        this.id = id;
        this.name = name;
        this.leaderId = leaderId;
        this.createdAt = createdAt;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UUID getLeaderId() {
        return leaderId;
    }

    public void setLeaderId(UUID leaderId) {
        this.leaderId = leaderId;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public List<Claim> getClaims() {
        return claims;
    }
}
