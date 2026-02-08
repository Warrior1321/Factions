package com.hytale.factions.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class BlockWorthConfig {
    private final Map<String, Integer> worthByBlock;

    private BlockWorthConfig(Map<String, Integer> worthByBlock) {
        this.worthByBlock = new HashMap<>(worthByBlock);
    }

    public static BlockWorthConfig load(Path path) {
        Map<String, Integer> values = new HashMap<>();
        try (BufferedReader reader = Files.newBufferedReader(path)) {
            String line;
            while ((line = reader.readLine()) != null) {
                String trimmed = line.trim();
                if (trimmed.isEmpty() || trimmed.startsWith("//") || trimmed.startsWith("{") || trimmed.startsWith("}")) {
                    continue;
                }
                String[] parts = trimmed.replace(",", "").replace("\"", "").split(":");
                if (parts.length == 2) {
                    String key = parts[0].trim();
                    String value = parts[1].trim();
                    try {
                        values.put(key, Integer.parseInt(value));
                    } catch (NumberFormatException ignored) {
                        // Invalid values are skipped; log in a real implementation.
                    }
                }
            }
        } catch (IOException ignored) {
            // Fall back to empty config; log in a real implementation.
        }
        return new BlockWorthConfig(values);
    }

    public int getWorth(String blockId) {
        return worthByBlock.getOrDefault(blockId, 0);
    }

    public Map<String, Integer> getAll() {
        return Collections.unmodifiableMap(worthByBlock);
    }
}
