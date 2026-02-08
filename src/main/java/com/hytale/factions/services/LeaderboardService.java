package com.hytale.factions.services;

import com.hytale.factions.models.Claim;
import com.hytale.factions.models.Faction;
import com.hytale.factions.models.LeaderboardEntry;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class LeaderboardService {
    private final FactionService factionService;
    private final ClaimService claimService;
    private final BlockWorthConfig blockWorthConfig;
    private final Map<UUID, Long> cachedScores = new ConcurrentHashMap<>();

    public LeaderboardService(FactionService factionService, ClaimService claimService, BlockWorthConfig blockWorthConfig) {
        this.factionService = factionService;
        this.claimService = claimService;
        this.blockWorthConfig = blockWorthConfig;
    }

    public void recalculateScores() {
        cachedScores.clear();
        for (Faction faction : factionService.getAllFactions()) {
            long score = calculateScoreForFaction(faction);
            cachedScores.put(faction.getId(), score);
        }
    }

    public List<LeaderboardEntry> getTop(int limit) {
        List<LeaderboardEntry> entries = new ArrayList<>();
        for (Faction faction : factionService.getAllFactions()) {
            long score = cachedScores.getOrDefault(faction.getId(), 0L);
            entries.add(new LeaderboardEntry(faction.getId(), faction.getName(), score));
        }
        entries.sort(Comparator.comparingLong(LeaderboardEntry::getScore).reversed());
        return entries.subList(0, Math.min(limit, entries.size()));
    }

    private long calculateScoreForFaction(Faction faction) {
        long total = 0L;
        for (Claim claim : claimService.getClaimsForFaction(faction.getId())) {
            total += scanClaimBlocks(claim);
        }
        return total;
    }

    private long scanClaimBlocks(Claim claim) {
        // TODO: Replace with Hytale world scanning API; this is a placeholder implementation.
        // In real code, iterate all blocks/spawners within the claim and sum blockWorthConfig values.
        return 0L;
    }

    public int getBlockWorth(String blockId) {
        return blockWorthConfig.getWorth(blockId);
    }
}
