package net.danh.bsoul.Manager;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class FileLoader {
    private List<Integer> blackListSlot = new ArrayList<>();
    private List<String> earnBlackListWorld = new ArrayList<>();
    private int defaultSoul = 5;
    private int defaultSoulMax = 10;
    private boolean debug = false;
    private boolean rehibilitate = true;
    private long time = 300;
    private int soul = 2;
    private int soulLost = 1;
    private boolean skipDeathScreen = true;
    private boolean loseItemWhenDeath = true;
    private boolean loseAllItem = false;
    private int minSoulToLose = 3;
    private boolean dropItem = true;
    private boolean preventMainHand = true;
    private boolean dropIncludeArmor = true;
    private boolean dropIncludeOffhand = true;
    private boolean moreDrops = true;
    private boolean pvp = true;
    private int killSoul = 1;
    private double chance = 54.9;
    private boolean soulItemStatus = true;
    private int soulSlot = 8;
    private ItemStack soulItem = null;
    private boolean mobKill = true;
    private boolean animalKill = false;
    private boolean monsterKill = true;


    public void load() {
        FileConfiguration config = Resources.getconfigfile();
        blackListSlot = config.getIntegerList("SETTINGS.BLACKLIST_SLOTS");
        earnBlackListWorld = config.getStringList("SETTINGS.EARN_BLACKLIST_WORLD");
        defaultSoul = config.getInt("SETTINGS.DEFAULT_SOUL", 5);
        defaultSoulMax = config.getInt("SETTINGS.DEFAULT_SOUL_MAX", 10);
        debug = config.getBoolean("SETTINGS.DEBUG", false);
        rehibilitate = config.getBoolean("REHIBILITATE.ENABLE", true);
        time = config.getInt("REHIBILITATE.TIME", 300);
        soul = config.getInt("REHIBILITATE.SOUL", 2);
        soulLost = config.getInt("DEATH.SOUL_LOST", 1);
        skipDeathScreen = config.getBoolean("DEATH.SKIP_DEATH_SCREEN", true);
        loseItemWhenDeath = config.getBoolean("DEATH.LOSE_ITEM_WHEN_DEATH", true);
        loseAllItem = config.getBoolean("DEATH.LOSE_ALL_ITEM", false);
        minSoulToLose = config.getInt("DEATH.MIN_SOUL_TO_LOSE", 3);
        dropItem = config.getBoolean("DEATH.DROP_ITEM", true);
        preventMainHand = config.getBoolean("DEATH.PREVENT_MAIN_HAND", true);
        dropIncludeArmor = config.getBoolean("DEATH.DROP.INCLUDE_ARMOR", true);
        dropIncludeOffhand = config.getBoolean("DEATH.DROP.INCLUDE_OFFHAND", true);
        moreDrops = config.getBoolean("DEATH.DROP.MORE_DROP.ENABLE", true);
        pvp = config.getBoolean("PVP.ENABLE", true);
        killSoul = config.getInt("PVP.KILL_SOUL", 1);
        chance = config.getDouble("PVP.CHANCE", 50);
        soulItemStatus = config.getBoolean("ITEM.SOUL.ENABLE", false);
        soulSlot = config.getInt("ITEM.SOUL.SLOT", 8);
        soulItem = Item.getSoulItems(1);
        mobKill = config.getBoolean("MOBS.ENABLE", true);
        animalKill = config.getBoolean("MOBS.ANIMAL", false);
        monsterKill = config.getBoolean("MOBS.MONSTER", true);
    }

    public List<Integer> getBlackListSlot() {
        return blackListSlot;
    }

    public List<String> getEarnBlackListWorld() {
        return earnBlackListWorld;
    }

    public int getDefaultSoul() {
        return defaultSoul;
    }

    public int getDefaultSoulMax() {
        return defaultSoulMax;
    }

    public boolean isDebug() {
        return debug;
    }

    public boolean isRehibilitate() {
        return rehibilitate;
    }

    public long getTime() {
        return time;
    }

    public int getSoul() {
        return soul;
    }

    public int getSoulLost() {
        return soulLost;
    }

    public boolean isSkipDeathScreen() {
        return skipDeathScreen;
    }

    public boolean isLoseItemWhenDeath() {
        return loseItemWhenDeath;
    }

    public boolean isLoseAllItem() {
        return loseAllItem;
    }

    public int getMinSoulToLose() {
        return minSoulToLose;
    }

    public boolean isDropItem() {
        return dropItem;
    }

    public boolean isPreventMainHand() {
        return preventMainHand;
    }

    public boolean isPvp() {
        return pvp;
    }

    public int getKillSoul() {
        return killSoul;
    }

    public double getChance() {
        return chance;
    }

    public boolean isSoulItemStatus() {
        return soulItemStatus;
    }

    public int getSoulSlot() {
        return soulSlot;
    }

    public ItemStack getSoulItem() {
        return soulItem;
    }

    public boolean isMobKill() {
        return mobKill;
    }

    public boolean isAnimalKill() {
        return animalKill;
    }

    public boolean isMonsterKill() {
        return monsterKill;
    }

    public boolean isDropIncludeArmor() {
        return dropIncludeArmor;
    }

    public boolean isDropIncludeOffhand() {
        return dropIncludeOffhand;
    }

    public boolean isMoreDrops() {
        return moreDrops;
    }
}
