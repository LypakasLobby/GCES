package com.lypaka.gces;

import com.lypaka.gces.Commands.GCESCommand;
import com.lypaka.gces.Listeners.ServerStartingListener;
import com.lypaka.gces.Modules.BattleModule;
import com.lypaka.gces.Modules.CatchingModule;
import com.lypaka.gces.Modules.Difficulty;
import com.lypaka.gces.Modules.LevelingModule;
import com.lypaka.lypakautils.ConfigurationLoaders.BasicConfigManager;
import com.lypaka.lypakautils.ConfigurationLoaders.ConfigUtils;
import com.lypaka.shadow.configurate.objectmapping.ObjectMappingException;
import com.lypaka.shadow.google.common.reflect.TypeToken;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class GCES implements ModInitializer {

    public static final String MOD_ID = "gces";
    public static final String MOD_NAME = "GCES";
    public static Logger logger = LogManager.getLogger(MOD_NAME);
    public static BasicConfigManager configManager;
    public static Map<String, Difficulty> difficultyMap = new HashMap<>();

    @Override
    public void onInitialize() {

        Path dir = ConfigUtils.checkDir(Paths.get("./config/gces"));
        String[] mainFiles = new String[]{"gces.conf", "player-accounts.conf"};
        configManager = new BasicConfigManager(mainFiles, dir, GCES.class, MOD_NAME, MOD_ID, logger);
        configManager.init();
        try {

            ConfigGetters.load();
            loadDifficulties();

        } catch (ObjectMappingException | IOException e) {

            throw new RuntimeException(e);

        }

        GCESCommand.register();
        ServerLifecycleEvents.SERVER_STARTING.register(new ServerStartingListener());

    }

    public static void loadDifficulties() throws IOException, ObjectMappingException {

        difficultyMap = new HashMap<>();
        logger.info("Loading difficulties...");
        String[] diffFiles = new String[]{"catching.conf", "leveling.conf", "battles.conf"};
        for (String diff : ConfigGetters.difficulties) {

            Path diffDir = ConfigUtils.checkDir(Paths.get("./config/gces/difficulties/" + diff));
            BasicConfigManager bcm = new BasicConfigManager(diffFiles, diffDir, GCES.class, MOD_NAME, MOD_ID, logger);
            bcm.init();

            String finalStagePermission = bcm.getConfigNode(0, "Settings", "Evolution-Stage", "Final").getString();
            String firstStagePermission = bcm.getConfigNode(0, "Settings", "Evolution-Stage", "First").getString();
            String middleStagePermission = bcm.getConfigNode(0, "Settings", "Evolution-Stage", "Middle").getString();
            String singleStagePermission = bcm.getConfigNode(0, "Settings", "Evolution-Stage", "Single").getString();
            String legendaryPermission = bcm.getConfigNode(0, "Settings", "Legendary-Permission").getString();
            String shinyPermission = bcm.getConfigNode(0, "Settings", "Shiny-Permission").getString();
            Map<String, Integer> catchingTierMap = bcm.getConfigNode(0, "Tiers").getValue(new TypeToken<Map<String, Integer>>() {});
            CatchingModule catchingModule = new CatchingModule(finalStagePermission, firstStagePermission, middleStagePermission, singleStagePermission, legendaryPermission, shinyPermission, catchingTierMap);

            Map<String, Integer> levelingTierMap = bcm.getConfigNode(1, "Tiers").getValue(new TypeToken<Map<String, Integer>>() {});
            LevelingModule levelingModule = new LevelingModule(levelingTierMap);

            boolean checkBattles = bcm.getConfigNode(2, "Check-Battles").getBoolean();
            BattleModule battleModule = new BattleModule(checkBattles);

            Difficulty difficulty = new Difficulty(diff, bcm, catchingModule, levelingModule, battleModule);
            difficulty.add();

        }

        logger.info("Successfully loaded all difficulties!");

    }

}
