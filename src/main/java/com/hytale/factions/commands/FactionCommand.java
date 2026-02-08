package com.hytale.factions.commands;

import com.hytale.factions.models.Claim;
import com.hytale.factions.models.ClaimType;
import com.hytale.factions.models.Faction;
import com.hytale.factions.models.LeaderboardEntry;
import com.hytale.factions.services.ClaimService;
import com.hytale.factions.services.FactionService;
import com.hytale.factions.services.LeaderboardService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class FactionCommand {
    private final FactionService factionService;
    private final ClaimService claimService;
    private final LeaderboardService leaderboardService;
    private final AdminModeTracker adminModeTracker;

    public FactionCommand(FactionService factionService, ClaimService claimService, LeaderboardService leaderboardService, AdminModeTracker adminModeTracker) {
        this.factionService = factionService;
        this.claimService = claimService;
        this.leaderboardService = leaderboardService;
        this.adminModeTracker = adminModeTracker;
    }

    public void execute(PlayerContext player, String[] args) {
        if (args.length == 0) {
            showHelp(player);
            return;
        }

        String sub = args[0].toLowerCase();
        switch (sub) {
            case "create" -> createFaction(player, args);
            case "delete" -> deleteFaction(player);
            case "help" -> showHelp(player);
            case "claim" -> openClaimUi(player);
            case "admin" -> toggleAdmin(player);
            case "top" -> showTop(player);
            case "calc" -> forceCalc(player);
            default -> showHelp(player);
        }
    }

    private void createFaction(PlayerContext player, String[] args) {
        if (args.length < 2) {
            player.sendMessage("Usage: /f create <name>");
            return;
        }
        Optional<Faction> existing = factionService.getFactionByPlayer(player.getId());
        if (existing.isPresent()) {
            player.sendMessage("You already have a faction.");
            return;
        }
        String name = String.join(" ", slice(args, 1));
        Faction faction = factionService.createFaction(name, player.getId());
        player.sendMessage("Faction created: " + faction.getName());
    }

    private void deleteFaction(PlayerContext player) {
        Optional<Faction> existing = factionService.getFactionByPlayer(player.getId());
        if (existing.isEmpty()) {
            player.sendMessage("You do not have a faction to delete.");
            return;
        }
        factionService.deleteFaction(existing.get().getId());
        player.sendMessage("Faction deleted.");
    }

    private void showHelp(PlayerContext player) {
        player.sendMessage("Faction Commands:");
        player.sendMessage("/f create <name> - Create a faction");
        player.sendMessage("/f delete - Delete your faction");
        player.sendMessage("/f claim - Open the claim UI");
        player.sendMessage("/f admin - Toggle admin claim mode");
        player.sendMessage("/f top - Show top 10 factions");
        player.sendMessage("/f calc - Force leaderboard calculation");
    }

    private void openClaimUi(PlayerContext player) {
        boolean isAdmin = adminModeTracker.isAdmin(player.getId());
        if (isAdmin) {
            player.openAdminClaimUi(claim -> claimService.addClaim(new Claim(
                    claim.getFactionId(),
                    claim.getWorldId(),
                    claim.getMinX(),
                    claim.getMinY(),
                    claim.getMinZ(),
                    claim.getMaxX(),
                    claim.getMaxY(),
                    claim.getMaxZ(),
                    claim.getType()
            )));
            return;
        }

        ClaimType type = ClaimType.NORMAL;
        player.openClaimUi(type, claim -> claimService.addClaim(new Claim(
                claim.getFactionId(),
                claim.getWorldId(),
                claim.getMinX(),
                claim.getMinY(),
                claim.getMinZ(),
                claim.getMaxX(),
                claim.getMaxY(),
                claim.getMaxZ(),
                type
        )));
    }

    private void toggleAdmin(PlayerContext player) {
        if (!player.hasPermission("factions.admin")) {
            player.sendMessage("You do not have permission to use admin mode.");
            return;
        }
        boolean enabled = adminModeTracker.toggle(player.getId());
        player.sendMessage("Admin claim mode: " + (enabled ? "enabled" : "disabled"));
    }

    private void showTop(PlayerContext player) {
        List<LeaderboardEntry> entries = leaderboardService.getTop(10);
        player.sendMessage("Top 10 Factions:");
        int index = 1;
        for (LeaderboardEntry entry : entries) {
            player.sendMessage(index + ". " + entry.getFactionName() + " - " + entry.getScore());
            index++;
        }
    }

    private void forceCalc(PlayerContext player) {
        if (!player.hasPermission("factions.calc")) {
            player.sendMessage("You do not have permission to recalculate the leaderboard.");
            return;
        }
        leaderboardService.recalculateScores();
        player.sendMessage("Leaderboard recalculated.");
    }

    private String[] slice(String[] args, int start) {
        String[] result = new String[args.length - start];
        System.arraycopy(args, start, result, 0, result.length);
        return result;
    }

    public interface PlayerContext {
        UUID getId();
        void sendMessage(String message);
        boolean hasPermission(String permission);
        void openClaimUi(ClaimType type, ClaimSelectionHandler handler);
        void openAdminClaimUi(AdminClaimSelectionHandler handler);
    }

    public interface ClaimSelectionHandler {
        void onClaimSelected(ClaimSelection selection);
    }

    public interface ClaimSelection {
        UUID getFactionId();
        String getWorldId();
        int getMinX();
        int getMinY();
        int getMinZ();
        int getMaxX();
        int getMaxY();
        int getMaxZ();
    }

    public interface AdminClaimSelectionHandler {
        void onClaimSelected(AdminClaimSelection selection);
    }

    public interface AdminClaimSelection extends ClaimSelection {
        ClaimType getType();
    }
}
