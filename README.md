# Faction Core Mod (Hytale)

This repository contains a starter Hytale mod implementation for a faction core system with `/f` commands, claim UI, admin safezone/warzone claiming, and a leaderboard calculated from block and spawner values.

## Features
- `/f create` — Create a faction.
- `/f delete` — Delete your faction.
- `/f help` — Show all commands.
- `/f claim` — Open the claim UI for players to claim a base.
- `/f admin` — Toggle admin mode; allows claiming safezones and warzones.
- `/f top` — Show the top 10 factions.
- `/f calc` — Force recalculation of the leaderboard (permission-gated).

## Configuration
- `config/blockworth.json` lets server owners define value per block or spawner.
- The leaderboard auto-updates every 30 minutes.

## Notes
This is a framework-ready skeleton. Hook the stubbed Hytale API calls into the real modding API and replace the placeholder UI hooks with the proper UI builder for Hytale.
