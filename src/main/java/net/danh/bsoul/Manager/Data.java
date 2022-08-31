package net.danh.bsoul.Manager;

import net.danh.bsoul.CustomEvents.SoulChangeEvent;
import net.danh.bsoul.Database.PlayerData;
import net.danh.bsoul.bSoul;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.SQLException;
import java.util.HashMap;

public class Data {
    static HashMap<String, Integer> soul = new HashMap<>();

    public static PlayerData getPlayerDatabase(Player player) throws SQLException {

        PlayerData playerStats = bSoul.db.getData(player.getName());

        if (playerStats == null) {
            playerStats = new PlayerData(player.getName(), 0, 1);
            bSoul.db.createTable(playerStats);
        }

        return playerStats;
    }

    public static int getSoulData(Player p) {
        try {
            return getPlayerDatabase(p).getdSoul();
        } catch (SQLException e) {
            return 0;
        }
    }

    public static int getSoul(Player p) {
        return soul.get(p.getName() + "_SOUL");
    }

    public static void setSoul(Player p, Integer amount) {
        if (getSoulData(p) == 0 && getMaxSoulData(p) == 0) {
            soul.put(p.getName() + "_SOUL", Math.max(Resources.getconfigfile().getInt("SETTINGS.DEFAULT_SOUL"), amount));
        } else {
            soul.put(p.getName() + "_SOUL", amount);
        }
        SoulChangeEvent soulChangeEvent = new SoulChangeEvent(p, Math.max(Resources.getconfigfile().getInt("SETTINGS.DEFAULT_SOUL"), amount));
        Bukkit.getServer().getPluginManager().callEvent(soulChangeEvent);
    }

    public static void addSoul(Player p, Integer amount) {
        int amounts = getSoul(p) + amount;
        if (amounts <= getSoulMax(p)) {
            soul.replace(p.getName() + "_SOUL", amounts);
            SoulChangeEvent soulChangeEvent = new SoulChangeEvent(p, amounts);
            Bukkit.getServer().getPluginManager().callEvent(soulChangeEvent);
        }
    }

    public static void removeSoul(Player p, Integer amount) {
        int count = getSoul(p) - amount;
        if (count >= 0) {
            soul.replace(p.getName() + "_SOUL", count);
            SoulChangeEvent soulChangeEvent = new SoulChangeEvent(p, count);
            Bukkit.getServer().getPluginManager().callEvent(soulChangeEvent);
        }
    }

    public static int getMaxSoulData(Player p) {
        try {
            return getPlayerDatabase(p).getmSoul();
        } catch (SQLException e) {
            return 0;
        }
    }

    public static int getSoulMax(Player p) {
        return soul.get(p.getName() + "_SOUL_MAX");
    }

    public static void setSoulMax(Player p, Integer amount) {
        if (getSoulData(p) == 0 && getMaxSoulData(p) == 0) {
            soul.put(p.getName() + "_SOUL_MAX", Math.max(Resources.getconfigfile().getInt("SETTINGS.DEFAULT_SOUL_MAX"), amount));
        } else {
            soul.put(p.getName() + "_SOUL_MAX", amount);
        }
    }

    public static void addSoulMax(Player p, Integer amount) {
        soul.replace(p.getName() + "_SOUL_MAX", getSoulMax(p) + amount);
    }

    public static void removeSoulMax(Player p, Integer amount) {
        if (getSoulMax(p) - amount >= 0) {
            soul.replace(p.getName() + "_SOUL_MAX", getSoulMax(p) - amount);
        }
    }
}
