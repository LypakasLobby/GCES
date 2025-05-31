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

public class CandyListener {

    public static void register() {

        CobblemonEvents.EXPERIENCE_CANDY_USE_PRE.subscribe(Priority.NORMAL, event -> {

            Pokemon pokemon = event.getPokemon();
            ServerPlayerEntity player = event.getPlayer();
            String diff = ConfigGetters.playerAccountsMap.get(player.getUuid().toString()).get("Difficulty");
            if (diff.equalsIgnoreCase("none")) return Unit.INSTANCE;
            Difficulty difficulty = GCES.difficultyMap.get(diff);
            LevelingModule levelingModule = difficulty.getLevelingModule();
            int tierLevel = Integer.parseInt(ConfigGetters.playerAccountsMap.get(player.getUuid().toString()).get("Leveling"));
            int maxLevel = levelingModule.getTierMap().get("Tier-" + tierLevel);
            int pokemonLevel = pokemon.getLevel();

            if (pokemonLevel >= maxLevel) {

                event.setExperienceYield(0);
                player.sendMessage(FancyTextHandler.getFormattedText(ConfigGetters.levelingTierMessage));

            }

            return Unit.INSTANCE;

        });

    }

}
