package com.lypaka.gces.Modules;

import java.util.Map;

public class LevelingModule {

    private final Map<String, Integer> tierMap;

    public LevelingModule (Map<String, Integer> tierMap) {

        this.tierMap = tierMap;

    }

    public Map<String, Integer> getTierMap() {

        return this.tierMap;

    }

}
