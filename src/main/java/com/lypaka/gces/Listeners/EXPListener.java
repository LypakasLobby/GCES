package com.lypaka.gces.Listeners;

import com.cobblemon.mod.common.api.Priority;
import com.cobblemon.mod.common.api.events.CobblemonEvents;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.lypaka.gces.ConfigGetters;
import com.lypaka.gces.GCES;
import com.lypaka.gces.Modules.Difficulty;
import com.lypaka.gces.Modules.LevelingModule;
import com.lypaka.lypakautils.Handlers.FancyTextHandler;
import kotlin.Unit;
import net.minecraft.server.network.ServerPlayerEntity;

public class EXPListener {

    public static void register() {

        CobblemonEvents.EXPERIENCE_GAINED_EVENT_PRE.subscribe(Priority.NORMAL, event -> {

            Pokemon pokemon = event.getPokemon();
            if (pokemon.isPlayerOwned()) {

                ServerPlayerEntity player = pokemon.getOwnerPlayer();
                if (player == null) {

                    GCES.logger.info("[GCES] An error was detected trying to get a player owner from a Pokemon. Printing debug info.");
                    GCES.logger.info(pokemon.getPersistentData());
                    GCES.logger.info("Player owned? " + pokemon.isPlayerOwned());
                    return Unit.INSTANCE;

                }
                String diff = ConfigGetters.playerAccountsMap.get(player.getUuid().toString()).get("Difficulty");
                if (diff.equalsIgnoreCase("none")) return Unit.INSTANCE;
                Difficulty difficulty = GCES.difficultyMap.get(diff);
                LevelingModule levelingModule = difficulty.getLevelingModule();
                int tierLevel = Integer.parseInt(ConfigGetters.playerAccountsMap.get(player.getUuid().toString()).get("Leveling"));
                int maxLevel = levelingModule.getTierMap().get("Tier-" + tierLevel);
                int pokemonLevel = pokemon.getLevel();
                int experience = event.getExperience();
                int experienceRequired = pokemon.getExperienceToLevel(maxLevel+1);
                if (pokemonLevel >= maxLevel || experience >= experienceRequired) {

                    event.cancel();
                    event.setExperience(Math.max(experienceRequired - 1, 0));
                    player.sendMessage(FancyTextHandler.getFormattedText(ConfigGetters.levelingTierMessage));

                }

            }

            return Unit.INSTANCE;

        });

    }

}
