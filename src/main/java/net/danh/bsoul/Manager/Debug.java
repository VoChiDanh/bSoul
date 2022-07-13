package net.danh.bsoul.Manager;

import net.danh.dcore.DCore;

public class Debug {

    public static void debug(String message) {
        if (Resources.getconfigfile().getBoolean("SETTINGS.DEBUG")) {
            DCore.dCoreLog(message);
        }
    }
}
