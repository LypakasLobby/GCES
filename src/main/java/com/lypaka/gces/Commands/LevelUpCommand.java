package com.lypaka.gces.Commands;

import com.lypaka.gces.ConfigGetters;
import com.lypaka.gces.GCES;
import com.lypaka.gces.Modules.CatchingModule;
import com.lypaka.gces.Modules.Difficulty;
import com.lypaka.gces.Modules.LevelingModule;
import com.lypaka.lypakautils.Handlers.FancyTextHandler;
import com.lypaka.lypakautils.Handlers.PermissionHandler;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.Map;

public class LevelUpCommand {

    public LevelUpCommand (CommandDispatcher<ServerCommandSource> dispatcher) {

        for (String a : GCESCommand.ALIASES) {

            dispatcher.register(
                    CommandManager.literal(a)
                            .then(
                                    CommandManager.argument("player", EntityArgumentType.players())
                                            .then(
                                                    CommandManager.argument("module", StringArgumentType.word())
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
                                                                String diff = ConfigGetters.playerAccountsMap.get(target.getUuid().toString()).get("Difficulty");
                                                                if (diff.equalsIgnoreCase("none")) {

                                                                    c.getSource().sendMessage(FancyTextHandler.getFormattedText("&eTarget player does not have a difficulty!"));
                                                                    return 0;

                                                                }

                                                                Difficulty difficulty = GCES.difficultyMap.get(diff);
                                                                int level;
                                                                int nextLevel;
                                                                if (module.equalsIgnoreCase("catching") || module.equalsIgnoreCase("catch")) {

                                                                    CatchingModule catchingModule = difficulty.getCatchingModule();
                                                                    Map<String, String> map = ConfigGetters.playerAccountsMap.get(target.getUuid().toString());
                                                                    level = Integer.parseInt(map.get("Catching"));
                                                                    nextLevel = level + 1;
                                                                    int maxLevel = catchingModule.getTierMap().size();
                                                                    if (nextLevel > maxLevel) {

                                                                        c.getSource().sendMessage(FancyTextHandler.getFormattedText("&eLevel cannot be higher than max level!"));
                                                                        return 0;

                                                                    }

                                                                    map.put("Catching", String.valueOf(nextLevel));
                                                                    ConfigGetters.playerAccountsMap.put(target.getUuid().toString(), map);
                                                                    target.sendMessage(FancyTextHandler.getFormattedText("&aYour tier in catching has increased to " + nextLevel + "!"));
                                                                    c.getSource().sendMessage(FancyTextHandler.getFormattedText("&eSuccessfully set " + target.getName() + "'s catching tier to " + nextLevel + "."));


                                                                } else if (module.equalsIgnoreCase("leveling") || module.equalsIgnoreCase("level")) {

                                                                    LevelingModule levelingModule = difficulty.getLevelingModule();
                                                                    Map<String, String> map = ConfigGetters.playerAccountsMap.get(target.getUuid().toString());
                                                                    level = Integer.parseInt(map.get("Leveling"));
                                                                    nextLevel = level + 1;
                                                                    int maxLevel = levelingModule.getTierMap().size();
                                                                    if (nextLevel > maxLevel) {

                                                                        c.getSource().sendMessage(FancyTextHandler.getFormattedText("&eLevel cannot be higher than max level!"));
                                                                        return 0;

                                                                    }

                                                                    map.put("Leveling", String.valueOf(nextLevel));
                                                                    ConfigGetters.playerAccountsMap.put(target.getUuid().toString(), map);
                                                                    target.sendMessage(FancyTextHandler.getFormattedText("&aYour tier in leveling has increased to " + nextLevel + "!"));
                                                                    c.getSource().sendMessage(FancyTextHandler.getFormattedText("&eSuccessfully set " + target.getName() + "'s leveling tier to " + nextLevel + "."));

                                                                } else {

                                                                    c.getSource().sendMessage(FancyTextHandler.getFormattedText("&cInvalid module!"));
                                                                    return 0;

                                                                }

                                                                return 1;

                                                            })
                                            )
                            )
            );

        }

    }

}
