package net.danh.bsoul.Database;

public class PlayerData {

    private final String name;
    private final int dSoul;
    private final int mSoul;

    public PlayerData(String name, int dSoul, int mSoul) {
        this.name = name;
        this.dSoul = dSoul;
        this.mSoul = mSoul;
    }

    public String getName() {
        return name;
    }

    public int getdSoul() {
        return dSoul;
    }

    public int getmSoul() {
        return mSoul;
    }
}
