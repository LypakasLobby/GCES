package com.lypaka.gces.Listeners;

import com.cobblemon.mod.common.api.Priority;
import com.cobblemon.mod.common.api.events.CobblemonEvents;
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.lypaka.gces.ConfigGetters;
import com.lypaka.gces.GCES;
import com.lypaka.gces.Modules.CatchingModule;
import com.lypaka.gces.Modules.Difficulty;
import com.lypaka.gces.Utils;
import kotlin.Unit;
import net.minecraft.server.network.ServerPlayerEntity;

public class SpawnListener {

    public static void register() {

        CobblemonEvents.POKEMON_ENTITY_SPAWN.subscribe(Priority.NORMAL, event -> {
            if (!ConfigGetters.scalePokemonSpawns)
                return Unit.INSTANCE;
            PokemonEntity entity = event.getEntity();
            if (event.getCtx().getCause().getEntity() instanceof ServerPlayerEntity) {
                ServerPlayerEntity player = (ServerPlayerEntity) event.getCtx().getCause().getEntity();
                if (player != null) {

                    String diff = ConfigGetters.playerAccountsMap.get(player.getUuid().toString()).get("Difficulty");
                    if (diff.equalsIgnoreCase("none")) return Unit.INSTANCE;
                    Difficulty difficulty = GCES.difficultyMap.get(diff);
                    CatchingModule module = difficulty.getCatchingModule();
                    int tierNum = Integer.parseInt(ConfigGetters.playerAccountsMap.get(player.getUuid().toString()).get("Catching"));
                    int maxLevel = module.getTierMap().get("Tier-" + tierNum);
                    String scale = ConfigGetters.pokemonSpawnScale;
                    Pokemon pokemon = entity.getPokemon();
                    pokemon.setLevel(Utils.getNewLevel(maxLevel, scale));

                }

            }

            return Unit.INSTANCE;

        });

    }

}
