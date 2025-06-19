package net.danh.bsoul.Manager;

import net.danh.bsoul.CustomEvents.SoulItemChangeEvent;
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
            playerStats = new PlayerData(player.getName(), new FileLoader().getDefaultSoul(), new FileLoader().getDefaultSoulMax());
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
        SoulItemChangeEvent soulItemChangeEvent;
        if (getSoulData(p) == 0 && getMaxSoulData(p) == 0) {
            soul.put(p.getName() + "_SOUL", Math.max(new FileLoader().getDefaultSoul(), amount));
            soulItemChangeEvent = new SoulItemChangeEvent(p, Math.max(new FileLoader().getDefaultSoul(), amount));
        } else {
            soul.put(p.getName() + "_SOUL", amount);
            soulItemChangeEvent = new SoulItemChangeEvent(p, amount);
        }
        Bukkit.getServer().getPluginManager().callEvent(soulItemChangeEvent);
    }

    public static boolean addSoul(Player p, Integer amount) {
        FileLoader fileLoader = new FileLoader();
        if (!fileLoader.getEarnBlackListWorld().contains(p.getWorld().getName())) {
            int amounts = getSoul(p) + amount;
            if (amounts <= getSoulMax(p)) {
                soul.replace(p.getName() + "_SOUL", amounts);
                SoulItemChangeEvent soulItemChangeEvent = new SoulItemChangeEvent(p, amounts);
                Bukkit.getServer().getPluginManager().callEvent(soulItemChangeEvent);
                return true;
            }
            return false;
        }
        return false;
    }

    public static void removeSoul(Player p, Integer amount) {
        int count = Math.max(getSoul(p) - amount, 0);
        soul.replace(p.getName() + "_SOUL", count);
        SoulItemChangeEvent soulItemChangeEvent = new SoulItemChangeEvent(p, count);
        Bukkit.getServer().getPluginManager().callEvent(soulItemChangeEvent);
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
            soul.put(p.getName() + "_SOUL_MAX", Math.max(new FileLoader().getDefaultSoulMax(), amount));
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

    public static int getLostAmount(Player p) {
        FileLoader fileLoader = new FileLoader();
        if (fileLoader.isMoreDrops()) {
            int soul = getSoul(p);
            if (Resources.getconfigfile().contains("DEATH.DROP.MORE_DROP.SOUL_" + soul)) {
                return Resources.getconfigfile().getInt("DEATH.DROP.MORE_DROP.SOUL_" + soul);
            } else return 1;
        }
        return 1;
    }
}
