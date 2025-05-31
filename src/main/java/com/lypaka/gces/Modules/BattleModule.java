package com.lypaka.gces.Modules;

public class BattleModule {

    private final boolean checkBattles;

    public BattleModule (boolean checkBattles) {

        this.checkBattles = checkBattles;

    }

    public boolean doCheckBattles() {

        return this.checkBattles;

    }

}
