package com.lypaka.gces.Listeners;

import com.lypaka.gces.ConfigGetters;
import com.lypaka.gces.Utils;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.server.MinecraftServer;

public class ServerStartingListener implements ServerLifecycleEvents.ServerStarting {

    @Override
    public void onServerStarting (MinecraftServer minecraftServer) {

        if (ConfigGetters.autoSave) {

            Utils.startTimer();

        }

        BattleStartListener.register();
        CandyListener.register();
        CaptureListener.register();
        EXPListener.register();
        LevelingListener.register();
        ServerPlayConnectionEvents.JOIN.register(new LoginListener());
        ServerLifecycleEvents.SERVER_STOPPING.register(new ServerShuttingDownListener());
        SpawnListener.register();

    }

}
