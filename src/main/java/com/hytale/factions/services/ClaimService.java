package com.hytale.factions.services;

import com.hytale.factions.models.Claim;
import com.hytale.factions.models.ClaimType;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ClaimService {
    private final List<Claim> claims = new ArrayList<>();

    public void addClaim(Claim claim) {
        claims.add(claim);
    }

    public List<Claim> getClaimsForFaction(UUID factionId) {
        List<Claim> result = new ArrayList<>();
        for (Claim claim : claims) {
            if (claim.getFactionId().equals(factionId)) {
                result.add(claim);
            }
        }
        return result;
    }

    public Optional<Claim> findClaim(String worldId, int x, int y, int z) {
        return claims.stream()
                .filter(claim -> claim.getWorldId().equals(worldId))
                .filter(claim -> x >= claim.getMinX() && x <= claim.getMaxX())
                .filter(claim -> y >= claim.getMinY() && y <= claim.getMaxY())
                .filter(claim -> z >= claim.getMinZ() && z <= claim.getMaxZ())
                .findFirst();
    }

    public boolean isProtected(String worldId, int x, int y, int z, UUID playerId, boolean isAdmin) {
        Optional<Claim> claim = findClaim(worldId, x, y, z);
        if (claim.isEmpty()) {
            return false;
        }
        ClaimType type = claim.get().getType();
        if (type == ClaimType.SAFEZONE) {
            return !isAdmin;
        }
        return type == ClaimType.NORMAL;
    }
}
