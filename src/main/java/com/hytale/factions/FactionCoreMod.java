package com.hytale.factions;

import com.hytale.factions.commands.AdminModeTracker;
import com.hytale.factions.commands.FactionCommand;
import com.hytale.factions.services.BlockWorthConfig;
import com.hytale.factions.services.ClaimService;
import com.hytale.factions.services.FactionService;
import com.hytale.factions.services.LeaderboardService;

import java.nio.file.Path;
import java.time.Duration;
import java.util.Timer;
import java.util.TimerTask;

public class FactionCoreMod {
    private final FactionService factionService = new FactionService();
    private final ClaimService claimService = new ClaimService();
    private LeaderboardService leaderboardService;
    private final AdminModeTracker adminModeTracker = new AdminModeTracker();
    private final Timer leaderboardTimer = new Timer("factions-leaderboard", true);

    public void onEnable(Path configDir) {
        BlockWorthConfig blockWorthConfig = BlockWorthConfig.load(configDir.resolve("blockworth.json"));
        leaderboardService = new LeaderboardService(factionService, claimService, blockWorthConfig);

        registerCommands();
        scheduleLeaderboardUpdates();
    }

    private void registerCommands() {
        FactionCommand command = new FactionCommand(factionService, claimService, leaderboardService, adminModeTracker);

        // TODO: Register this command with the Hytale command API.
        // Example: commandRegistry.register("f", command::execute);
    }

    private void scheduleLeaderboardUpdates() {
        long intervalMs = Duration.ofMinutes(30).toMillis();
        leaderboardTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                leaderboardService.recalculateScores();
            }
        }, intervalMs, intervalMs);
    }
}
