package com.hytale.factions.services;

import com.hytale.factions.models.Faction;

import java.time.Instant;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class FactionService {
    private final Map<UUID, Faction> factionsById = new HashMap<>();
    private final Map<UUID, UUID> factionByPlayer = new HashMap<>();

    public Optional<Faction> getFactionByPlayer(UUID playerId) {
        UUID factionId = factionByPlayer.get(playerId);
        if (factionId == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(factionsById.get(factionId));
    }

    public Optional<Faction> getFaction(UUID factionId) {
        return Optional.ofNullable(factionsById.get(factionId));
    }

    public Faction createFaction(String name, UUID leaderId) {
        UUID id = UUID.randomUUID();
        Faction faction = new Faction(id, name, leaderId, Instant.now());
        factionsById.put(id, faction);
        factionByPlayer.put(leaderId, id);
        return faction;
    }

    public void deleteFaction(UUID factionId) {
        Faction faction = factionsById.remove(factionId);
        if (faction != null) {
            factionByPlayer.values().removeIf(id -> id.equals(factionId));
        }
    }

    public Collection<Faction> getAllFactions() {
        return factionsById.values();
    }
}
