package com.lypaka.gces.Listeners;

import com.cobblemon.mod.common.api.Priority;
import com.cobblemon.mod.common.api.events.CobblemonEvents;
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.lypaka.gces.ConfigGetters;
import com.lypaka.gces.GCES;
import com.lypaka.gces.Modules.CatchingModule;
import com.lypaka.gces.Modules.Difficulty;
import com.lypaka.lypakautils.Handlers.FancyTextHandler;
import com.lypaka.lypakautils.Handlers.PermissionHandler;
import kotlin.Unit;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;

public class CaptureListener {

    public static void register() {

        CobblemonEvents.THROWN_POKEBALL_HIT.subscribe(Priority.NORMAL, event -> {

            PokemonEntity entity = event.getPokemon();
            Pokemon pokemon = entity.getPokemon();
            if (event.getPokeBall().getOwner() instanceof ServerPlayerEntity) {

                ServerPlayerEntity player = (ServerPlayerEntity) event.getPokeBall().getOwner();
                String diff = ConfigGetters.playerAccountsMap.get(player.getUuid().toString()).get("Difficulty");
                if (diff.equalsIgnoreCase("none")) return Unit.INSTANCE;
                Difficulty difficulty = GCES.difficultyMap.get(diff);
                CatchingModule catchingModule = difficulty.getCatchingModule();
                ItemStack ball = event.getPokeBall().getPokeBall().item().getDefaultStack();
                ball.setCount(1);

                if (pokemon.getShiny()) {

                    if (!catchingModule.getShinyPermission().equals("")) {

                        if (!PermissionHandler.hasPermission(player, catchingModule.getShinyPermission())) {

                            event.cancel();
                            player.sendMessage(FancyTextHandler.getFormattedText(ConfigGetters.missingPermissionMessage));
                            player.giveItemStack(ball);
                            return Unit.INSTANCE;

                        }

                    }

                }

                if (pokemon.isLegendary() || pokemon.isMythical() || pokemon.isUltraBeast()) {

                    if (!catchingModule.getLegendaryPermission().equals("")) {

                        if (!PermissionHandler.hasPermission(player, catchingModule.getLegendaryPermission())) {

                            event.cancel();
                            player.sendMessage(FancyTextHandler.getFormattedText(ConfigGetters.missingPermissionMessage));
                            player.giveItemStack(ball);
                            return Unit.INSTANCE;

                        }

                    }

                }

                String evoStage = getEvoStage(pokemon);
                String perm;
                switch (evoStage) {

                    case "Final":
                        perm = catchingModule.getFinalStagePermission();
                        break;

                    case "First":
                        perm = catchingModule.getFirstStagePermission();
                        break;

                    case "Middle":
                        perm = catchingModule.getMiddleStagePermission();
                        break;

                    default:
                        perm = catchingModule.getSingleStagePermission();
                        break;

                }

                if (!perm.equals("")) {

                    if (!PermissionHandler.hasPermission(player, perm)) {

                        event.cancel();
                        player.sendMessage(FancyTextHandler.getFormattedText(ConfigGetters.missingPermissionMessage));
                        player.giveItemStack(ball);
                        return Unit.INSTANCE;

                    }

                }

                int tierLevel = Integer.parseInt(ConfigGetters.playerAccountsMap.get(player.getUuid().toString()).get("Catching"));
                int maxLevel = catchingModule.getTierMap().get("Tier-" + tierLevel);
                if (pokemon.getLevel() > maxLevel) {

                    event.cancel();
                    player.sendMessage(FancyTextHandler.getFormattedText(ConfigGetters.catchingTierMessage));
                    player.giveItemStack(ball);

                }

            }

            return Unit.INSTANCE;

        });

    }

    private static String getEvoStage (Pokemon pokemon) {

        // Pokemon has no pre-evolutions and can evolve, Pokemon is baby-stage
        if (pokemon.getForm().getPreEvolution() == null && pokemon.getForm().getEvolutions().size() != 0) {

            return "First";

        }

        // Pokemon has pre-evolutions and can evolve, Pokemon is middle-stage
        if (pokemon.getForm().getPreEvolution() != null && pokemon.getForm().getEvolutions().size() != 0) {

            return "Middle";

        }

        // Pokemon has pre-evolutions and can not evolve, Pokemon is final-stage
        if (pokemon.getForm().getPreEvolution() != null && pokemon.getForm().getEvolutions().size() == 0) {

            return "Final";

        }

        // Pokemon has no pre-evolutions and can not evolve, Pokemon is single-stage
        if (pokemon.getForm().getPreEvolution() == null && pokemon.getForm().getEvolutions().size() == 0) {

            return "Single";

        }

        return "None";

    }

}
