package net.danh.bsoul.Manager;

import net.danh.bsoul.bSoul;

import java.util.Collections;
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

    public static void update29() {
        if (!Resources.getconfigfile().contains("DEATH.DROP_ITEM")) {
            Resources.getconfigfile().set("DEATH.DROP_ITEM", true);
            Resources.saveconfig();
            bSoul.getInstance().getLogger().log(Level.WARNING, "Config has been updated for version 2.9!");
        }
    }

    public static void update295() {
        if (!Resources.getconfigfile().contains("SETTINGS.EARN_BLACKLIST_WORLD")) {
            Resources.getconfigfile().set("SETTINGS.EARN_BLACKLIST_WORLD", Collections.singletonList("example_world"));
            Resources.saveconfig();
            bSoul.getInstance().getLogger().log(Level.WARNING, "Config has been updated for version 2.9.5!");
        }
    }


    public static void update299() {
        if (!Resources.getconfigfile().contains("DEATH.PREVENT_MAIN_HAND")) {
            Resources.getconfigfile().set("DEATH.PREVENT_MAIN_HAND", true);
            Resources.saveconfig();
            bSoul.getInstance().getLogger().log(Level.WARNING, "Config has been updated for version 2.9.9!");
        }
    }

    public static void debug(String message) {
        if (new FileLoader().isDebug()) {
            bSoul.getInstance().getLogger().warning(message);
        }
    }
}
