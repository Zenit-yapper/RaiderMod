package com.voracious.hub;

import com.voracious.hub.api.TierAPI;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.minecraft.client.network.PlayerListEntry;

import java.util.Collection;

public class RaiderHubClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // Clear the cache on join to ensure fresh data
        TierAPI.clearCache();

        // Register a ClientPlayConnectionEvents.JOIN listener
        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
            // Trigger the initial TierAPI.fetch() for all players in the tab list when joining a server
            if (client.getNetworkHandler() != null) {
                Collection<PlayerListEntry> playerList = client.getNetworkHandler().getPlayerList();
                for (PlayerListEntry entry : playerList) {
                    if (entry.getProfile() != null && entry.getProfile().getName() != null) {
                        TierAPI.fetch(entry.getProfile().getName());
                    }
                }
            }
        });
    }
}
