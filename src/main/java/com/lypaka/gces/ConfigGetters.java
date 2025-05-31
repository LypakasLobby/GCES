package com.lypaka.gces;


import com.lypaka.shadow.configurate.objectmapping.ObjectMappingException;
import com.lypaka.shadow.google.common.reflect.TypeToken;

import java.util.List;
import java.util.Map;

public class ConfigGetters {

    public static List<String> difficulties;
    public static String defaultDifficulty;
    public static boolean restrictionOptional;
    public static String pokemonSpawnScale;
    public static boolean scalePokemonSpawns;
    public static String battleMessage;
    public static String catchingTierMessage;
    public static String dynamaxMessage;
    public static String levelingTierMessage;
    public static String megaMessage;
    public static String missingPermissionMessage;
    public static boolean autoSave;
    public static int saveInterval;

    public static Map<String, Map<String, String>> playerAccountsMap;

    public static void load() throws ObjectMappingException {

        difficulties = GCES.configManager.getConfigNode(0, "General-Settings", "Difficulties").getList(TypeToken.of(String.class));
        defaultDifficulty = GCES.configManager.getConfigNode(0, "General-Settings", "Restrictions", "Default-Difficulty").getString();
        restrictionOptional = GCES.configManager.getConfigNode(0, "General-Settings", "Restrictions", "Optional").getBoolean();
        pokemonSpawnScale = GCES.configManager.getConfigNode(0, "General-Settings", "Spawn-Settings", "Pokemon-Scale").getString();
        scalePokemonSpawns = GCES.configManager.getConfigNode(0, "General-Settings", "Spawn-Settings", "Scale-Wild-Pokemon").getBoolean();
        battleMessage = GCES.configManager.getConfigNode(0, "Messages", "Battle-Error").getString();
        catchingTierMessage = GCES.configManager.getConfigNode(0, "Messages", "Catching-Tier-Error").getString();

        dynamaxMessage = GCES.configManager.getConfigNode(0, "Messages", "Dynamax-Error").getString();
        levelingTierMessage = GCES.configManager.getConfigNode(0, "Messages", "Leveling-Tier-Error").getString();
        megaMessage = GCES.configManager.getConfigNode(0, "Messages", "Mega-Error").getString();
        missingPermissionMessage = GCES.configManager.getConfigNode(0, "Messages", "Missing-Permission").getString();
        autoSave = GCES.configManager.getConfigNode(0, "Task", "Enable-Auto-Saves").getBoolean();
        saveInterval = GCES.configManager.getConfigNode(0, "Task", "Interval").getInt();

        playerAccountsMap = GCES.configManager.getConfigNode(1, "Accounts").getValue(new TypeToken<Map<String, Map<String, String>>>() {});

    }

}
