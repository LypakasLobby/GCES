package com.lypaka.gces.Listeners;

import com.lypaka.gces.ConfigGetters;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.HashMap;
import java.util.Map;

public class LoginListener implements ServerPlayConnectionEvents.Join {

    @Override
    public void onPlayReady (ServerPlayNetworkHandler serverPlayNetworkHandler, PacketSender packetSender, MinecraftServer minecraftServer) {

        ServerPlayerEntity player = serverPlayNetworkHandler.getPlayer();
        if (!ConfigGetters.playerAccountsMap.containsKey(player.getUuid().toString())) {

            Map<String, String> map = new HashMap<>();
            if (ConfigGetters.restrictionOptional) {

                map.put("Difficulty", "none");

            } else {

                map.put("Difficulty", ConfigGetters.defaultDifficulty);

            }

            map.put("Catching", "1");
            map.put("Leveling", "1");
            ConfigGetters.playerAccountsMap.put(player.getUuid().toString(), map);

        }

    }

}
