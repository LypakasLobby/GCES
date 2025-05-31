package com.lypaka.gces.Commands;

import com.lypaka.gces.ConfigGetters;
import com.lypaka.gces.GCES;
import com.lypaka.gces.Utils;
import com.lypaka.lypakautils.Handlers.FancyTextHandler;
import com.lypaka.lypakautils.Handlers.PermissionHandler;
import com.lypaka.shadow.configurate.objectmapping.ObjectMappingException;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

import java.io.IOException;
import java.util.Map;

public class ReloadCommand {

    public ReloadCommand (CommandDispatcher<ServerCommandSource> dispatcher) {

        for (String a : GCESCommand.ALIASES) {

            dispatcher.register(
                    CommandManager.literal("gces")
                            .then(
                                    CommandManager.literal("reload")
                                            .executes(c -> {

                                                if (c.getSource().getEntity() instanceof ServerPlayerEntity) {

                                                    ServerPlayerEntity player = (ServerPlayerEntity) c.getSource().getEntity();
                                                    if (!PermissionHandler.hasPermission(player, "gces.command.admin")) {

                                                        player.sendMessage(FancyTextHandler.getFormattedText("&cYou don't have permission to use this command!"));
                                                        return 0;

                                                    }

                                                }

                                                try {

                                                    Map<String, Map<String, String>> playerAccountsMap = ConfigGetters.playerAccountsMap; // a very stupid fix for a very stupid problem
                                                    GCES.configManager.load();
                                                    ConfigGetters.load();
                                                    ConfigGetters.playerAccountsMap = playerAccountsMap;
                                                    GCES.loadDifficulties();
                                                    if (ConfigGetters.autoSave) {

                                                        Utils.startTimer();

                                                    }
                                                    c.getSource().sendMessage(FancyTextHandler.getFormattedText("&aSuccessfully reloaded GCES configuration!"));

                                                } catch (IOException | ObjectMappingException e) {

                                                    e.printStackTrace();

                                                }
                                                return 1;

                                            })

                            )

            );

        }

    }

}
