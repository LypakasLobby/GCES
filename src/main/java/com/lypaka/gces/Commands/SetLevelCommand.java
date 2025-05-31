package com.lypaka.gces.Commands;

import com.lypaka.gces.ConfigGetters;
import com.lypaka.gces.GCES;
import com.lypaka.gces.Modules.CatchingModule;
import com.lypaka.gces.Modules.Difficulty;
import com.lypaka.gces.Modules.LevelingModule;
import com.lypaka.lypakautils.Handlers.FancyTextHandler;
import com.lypaka.lypakautils.Handlers.PermissionHandler;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.Map;

public class SetLevelCommand {

    public SetLevelCommand (CommandDispatcher<ServerCommandSource> dispatcher) {

        for (String a : GCESCommand.ALIASES) {

            dispatcher.register(
                    CommandManager.literal(a)
                            .then(
                                    CommandManager.literal("setlvl")
                                            .then(
                                                    CommandManager.argument("player", EntityArgumentType.player())
                                                            .then(
                                                                    CommandManager.argument("module", StringArgumentType.word())
                                                                            .then(
                                                                                    CommandManager.argument("level", IntegerArgumentType.integer(1))
                                                                                            .executes(c -> {

                                                                                                if (c.getSource().getEntity() instanceof ServerPlayerEntity) {

                                                                                                    ServerPlayerEntity player = (ServerPlayerEntity) c.getSource().getEntity();
                                                                                                    if (!PermissionHandler.hasPermission(player, "gces.command.admin")) {

                                                                                                        player.sendMessage(FancyTextHandler.getFormattedText("&cYou don't have permission to use this command!"));
                                                                                                        return 0;

                                                                                                    }

                                                                                                }

                                                                                                ServerPlayerEntity target = EntityArgumentType.getPlayer(c, "player");
                                                                                                String module = StringArgumentType.getString(c, "module");
                                                                                                int level = IntegerArgumentType.getInteger(c, "level");

                                                                                                if (!ConfigGetters.playerAccountsMap.containsKey(target.getUuid().toString())) {

                                                                                                    c.getSource().sendMessage(FancyTextHandler.getFormattedText("&eTarget player does not have an account, which is not a good thing!"));
                                                                                                    return 0;

                                                                                                }

                                                                                                String diff = ConfigGetters.playerAccountsMap.get(target.getUuid().toString()).get("Difficulty");
                                                                                                if (diff.equalsIgnoreCase("none")) {

                                                                                                    c.getSource().sendMessage(FancyTextHandler.getFormattedText("&eTarget player does not have a difficulty!"));
                                                                                                    return 0;

                                                                                                }

                                                                                                Difficulty difficulty = GCES.difficultyMap.get(diff);
                                                                                                if (module.equalsIgnoreCase("level") || module.equalsIgnoreCase("leveling")) {

                                                                                                    LevelingModule levelingModule = difficulty.getLevelingModule();
                                                                                                    int maxLevel = levelingModule.getTierMap().size();
                                                                                                    if (level > maxLevel) {

                                                                                                        c.getSource().sendMessage(FancyTextHandler.getFormattedText("&eLevel cannot be higher than max level!"));
                                                                                                        return 0;

                                                                                                    }

                                                                                                    Map<String, String> map = ConfigGetters.playerAccountsMap.get(target.getUuid().toString());
                                                                                                    map.put("Leveling", String.valueOf(level));
                                                                                                    ConfigGetters.playerAccountsMap.put(target.getUuid().toString(), map);
                                                                                                    c.getSource().sendMessage(FancyTextHandler.getFormattedText("&eSuccessfully set " + target.getName() + "'s leveling tier to " + level + "."));
                                                                                                    return 1;

                                                                                                } else if (module.equalsIgnoreCase("catch") || module.equalsIgnoreCase("catching")) {

                                                                                                    CatchingModule catchingModule = difficulty.getCatchingModule();
                                                                                                    int maxLevel = catchingModule.getTierMap().size();
                                                                                                    if (level > maxLevel) {

                                                                                                        c.getSource().sendMessage(FancyTextHandler.getFormattedText("&eLevel cannot be higher than max level!"));
                                                                                                        return 0;

                                                                                                    }

                                                                                                    Map<String, String> map = ConfigGetters.playerAccountsMap.get(target.getUuid().toString());
                                                                                                    map.put("Catching", String.valueOf(level));
                                                                                                    ConfigGetters.playerAccountsMap.put(target.getUuid().toString(), map);
                                                                                                    c.getSource().sendMessage(FancyTextHandler.getFormattedText("&eSuccessfully set " + target.getName() + "'s catching tier to " + level + "."));
                                                                                                    return 1;

                                                                                                } else {

                                                                                                    c.getSource().sendMessage(FancyTextHandler.getFormattedText("&cInvalid module! Use \"catching\" or \"leveling\"!"));
                                                                                                    return 0;

                                                                                                }

                                                                                            })

                                                                            )

                                                            )

                                            )

                            )

            );

        }

    }

}
