package com.lypaka.gces.Listeners;

import com.lypaka.gces.ConfigGetters;
import com.lypaka.gces.GCES;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;

public class ServerShuttingDownListener implements ServerLifecycleEvents.ServerStopping {

    @Override
    public void onServerStopping (MinecraftServer minecraftServer) {

        GCES.configManager.getConfigNode(1, "Accounts").setValue(ConfigGetters.playerAccountsMap);
        GCES.configManager.save();

    }

}
