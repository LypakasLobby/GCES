package com.lypaka.gces.Modules;

import com.lypaka.gces.GCES;
import com.lypaka.lypakautils.ConfigurationLoaders.BasicConfigManager;

public class Difficulty {

    private final String difficulty;
    private final BasicConfigManager configManager;
    private final CatchingModule catchingModule;
    private final LevelingModule levelingModule;
    private final BattleModule battleModule;

    public Difficulty (String difficulty, BasicConfigManager configManager, CatchingModule catchingModule, LevelingModule levelingModule, BattleModule battleModule) {

        this.difficulty = difficulty;
        this.configManager = configManager;
        this.catchingModule = catchingModule;
        this.levelingModule = levelingModule;
        this.battleModule = battleModule;

    }

    public void add() {

        GCES.difficultyMap.put(this.difficulty, this);

    }

    public String getDifficulty() {

        return this.difficulty;

    }

    public BasicConfigManager getConfigManager() {

        return this.configManager;

    }

    public CatchingModule getCatchingModule() {

        return this.catchingModule;

    }

    public LevelingModule getLevelingModule() {

        return this.levelingModule;

    }

    public BattleModule getBattleModule() {

        return this.battleModule;

    }

}
