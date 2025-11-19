package com.lypaka.gces;

import com.cobblemon.mod.common.Cobblemon;
import com.lypaka.lypakautils.Handlers.RandomHandler;

import java.util.Timer;
import java.util.TimerTask;

public class Utils {

    public static Timer timer = null;

    public static void startTimer() {

        if (timer != null) {

            timer.cancel();

        }
        timer = new Timer();
        long interval = ConfigGetters.saveInterval * 1000L;
        timer.schedule(new TimerTask() {

            @Override
            public void run() {

                GCES.logger.info("Starting save...");
                GCES.configManager.getConfigNode(1, "Accounts").setValue(ConfigGetters.playerAccountsMap);
                GCES.configManager.save();
                GCES.logger.info("Save completed.");

            }

        }, 0, interval);

    }

    public static int getNewLevel (int maxLevel, String scale) {

        String[] split = scale.split(" ");
        String function = split[0];
        String amount = split[1];
        int maxAmount;

        if (amount.contains("r")) {

            maxAmount = RandomHandler.getRandomNumberBetween(1, Integer.parseInt(amount.replace("r", "")));

        } else {

            maxAmount = Integer.parseInt(amount);

        }

        int newLevel;
        switch (function) {

            case "+-":
                if (RandomHandler.getRandomChance(50)) {

                    newLevel = Math.min(Cobblemon.INSTANCE.getConfig().getMaxPokemonLevel(), maxAmount + maxLevel);

                } else {

                    int value;
                    if (maxLevel > maxAmount) {

                        value = maxLevel - maxAmount;

                    } else {

                        value = maxAmount - maxLevel;

                    }
                    newLevel = Math.max(1, value);

                }
                break;

            case "+":
                newLevel = Math.min(Cobblemon.INSTANCE.getConfig().getMaxPokemonLevel(), maxAmount + maxLevel);
                break;

            default:
                int value;
                if (maxLevel > maxAmount) {

                    value = maxLevel - maxAmount;

                } else {

                    value = maxAmount - maxLevel;

                }
                newLevel = Math.max(1, value);
                break;

        }

        return newLevel;

    }

}
