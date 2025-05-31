package com.lypaka.gces.Commands;

import com.lypaka.gces.ConfigGetters;
import com.lypaka.lypakautils.Handlers.FancyTextHandler;
import com.lypaka.lypakautils.Handlers.PermissionHandler;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.Map;

public class SetDifficultyCommand {

    public SetDifficultyCommand (CommandDispatcher<ServerCommandSource> dispatcher) {

        for (String a : GCESCommand.ALIASES) {

            dispatcher.register(
                    CommandManager.literal(a)
                            .then(
                                    CommandManager.literal("setdiff")
                                            .then(
                                                    CommandManager.argument("player", EntityArgumentType.player())
                                                            .then(
                                                                    CommandManager.argument("difficulty", StringArgumentType.word())
                                                                            .executes(c -> {

                                                                                if (c.getSource().getEntity() instanceof ServerPlayerEntity) {

                                                                                    ServerPlayerEntity player = (ServerPlayerEntity) c.getSource().getEntity();
                                                                                    if (!PermissionHandler.hasPermission(player, "gces.command.admin")) {

                                                                                        player.sendMessage(FancyTextHandler.getFormattedText("&cYou don't have permission to use this command!"));
                                                                                        return 0;

                                                                                    }

                                                                                }

                                                                                ServerPlayerEntity target = EntityArgumentType.getPlayer(c, "player");
                                                                                String diff = StringArgumentType.getString(c, "difficulty");
                                                                                String difficulty = null;

                                                                                for (String d : ConfigGetters.difficulties) {

                                                                                    if (d.equalsIgnoreCase(diff)) {

                                                                                        difficulty = d;
                                                                                        break;

                                                                                    }

                                                                                }

                                                                                if (difficulty == null) {

                                                                                    c.getSource().sendMessage(FancyTextHandler.getFormattedText("&cInvalid difficulty!"));
                                                                                    return 0;

                                                                                }

                                                                                Map<String, String> map = ConfigGetters.playerAccountsMap.get(target.getUuid().toString());
                                                                                map.put("Difficulty", difficulty);
                                                                                ConfigGetters.playerAccountsMap.put(target.getUuid().toString(), map);
                                                                                target.sendMessage(FancyTextHandler.getFormattedText("&eYour difficulty has been set to: " + difficulty));
                                                                                c.getSource().sendMessage(FancyTextHandler.getFormattedText("&aSuccessfully set " + target.getName() + "'s difficulty to " + difficulty + "."));
                                                                                return 1;

                                                                            })
                                                            )
                                            )
                            )
            );

        }

    }

}
