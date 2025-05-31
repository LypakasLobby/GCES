package com.lypaka.gces.Commands;


import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

import java.util.Arrays;
import java.util.List;

public class GCESCommand {

    public static final List<String> ALIASES = Arrays.asList("gces", "gottacatchemsmall");

    public static void register() {

        CommandRegistrationCallback.EVENT.register((dispatcher,
                                                    registryAccess,
                                                    environment) -> {

            new LevelUpCommand(dispatcher);
            new ReloadCommand(dispatcher);
            new SetDifficultyCommand(dispatcher);
            new SetLevelCommand(dispatcher);

        });

    }

}
