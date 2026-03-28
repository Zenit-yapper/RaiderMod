package com.voracious.hub; 

import com.voracious.hub.api.TierAPI;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.minecraft.client.network.PlayerListEntry;
import java.util.Collection;

public class RaiderHubClient implements ModInitializer, ClientModInitializer {

    @Override
    public void onInitialize() {
        // Essential safety net for the "Main Class" error
    }

    @Override
    public void onInitializeClient() {
        TierAPI.clearCache();
        
        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
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
