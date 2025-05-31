package com.lypaka.gces.Listeners;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.api.Priority;
import com.cobblemon.mod.common.api.battles.model.PokemonBattle;
import com.cobblemon.mod.common.api.battles.model.actor.ActorType;
import com.cobblemon.mod.common.api.battles.model.actor.BattleActor;
import com.cobblemon.mod.common.api.events.CobblemonEvents;
import com.cobblemon.mod.common.api.storage.party.PlayerPartyStore;
import com.cobblemon.mod.common.battles.actor.PlayerBattleActor;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.lypaka.gces.ConfigGetters;
import com.lypaka.gces.GCES;
import com.lypaka.gces.Modules.BattleModule;
import com.lypaka.gces.Modules.Difficulty;
import com.lypaka.gces.Modules.LevelingModule;
import com.lypaka.lypakautils.Handlers.FancyTextHandler;
import kotlin.Unit;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.Map;

public class BattleStartListener {

    public static void register() {

        CobblemonEvents.BATTLE_STARTED_PRE.subscribe(Priority.NORMAL, event -> {

            PokemonBattle battle = event.getBattle();
            for (BattleActor actor : battle.getActors()) {

                if (actor.getType() == ActorType.PLAYER) {

                    ServerPlayerEntity player = ((PlayerBattleActor) actor).getEntity();
                    String diff = ConfigGetters.playerAccountsMap.get(player.getUuid().toString()).get("Difficulty");
                    if (!diff.equalsIgnoreCase("none")) {

                        Difficulty difficulty = GCES.difficultyMap.get(diff);
                        BattleModule battleModule = difficulty.getBattleModule();
                        if (!battleModule.doCheckBattles()) return Unit.INSTANCE;

                        PlayerPartyStore storage = Cobblemon.INSTANCE.getStorage().getParty(player);
                        int maxLevel = 0;
                        for (int i = 0; i < 6; i++) {

                            Pokemon pokemon = storage.get(i);
                            if (pokemon != null) {

                                int lvl = pokemon.getLevel();
                                if (lvl > maxLevel) {

                                    maxLevel = lvl;

                                }

                            }

                        }
                        LevelingModule levelingModule = difficulty.getLevelingModule();
                        Map<String, String> map = ConfigGetters.playerAccountsMap.get(player.getUuid().toString());
                        int levelingLevel = Integer.parseInt(map.get("Leveling"));
                        int maxLevelingLevel = levelingModule.getTierMap().get("Tier-" + levelingLevel);
                        if (maxLevel > maxLevelingLevel) {

                            event.cancel();
                            player.sendMessage(FancyTextHandler.getFormattedText(ConfigGetters.battleMessage));

                        }

                    }


                }

            }

            return Unit.INSTANCE;

        });

    }

}
