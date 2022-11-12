package net.danh.bsoul.Manager;

import net.danh.bsoul.bSoul;
import net.danh.dcore.DCore;

import java.util.List;
import java.util.logging.Level;

public class Debug {

    public static void update27() {
        List<Integer> update = Resources.getconfigfile().getIntegerList("SETTINGS.BLACKLIST_SLOTS");
        if (!Resources.getconfigfile().contains("SETTINGS.BLACKLIST_SLOTS") || update.isEmpty()) {
            bSoul.getInstance().getLogger().log(Level.WARNING, "You need add BLACKLIST_SLOTS below SETTINGS (config.yml) to get update 2.7");
            bSoul.getInstance().getLogger().log(Level.WARNING, "Open jar with winrar to check full config!");
        }
    }

    public static void debug(String message) {
        if (Resources.getconfigfile().getBoolean("SETTINGS.DEBUG")) {
            DCore.dCoreLog(message);
        }
    }
}
