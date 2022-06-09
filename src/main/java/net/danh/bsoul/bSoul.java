package net.danh.bsoul;

import net.danh.bsoul.Cmd.Soul;
import net.danh.bsoul.Events.JoinQuit;
import net.danh.bsoul.Events.MobDeath;
import net.danh.bsoul.Events.PlayerDeath;
import net.danh.bsoul.Events.SoulChange;
import net.danh.bsoul.Hook.Placeholder;
import net.danh.bsoul.Manager.Data;
import net.danh.bsoul.Manager.Resources;
import net.danh.dcore.DCore;
import net.danh.dcore.Utils.File;
import org.bstats.bukkit.Metrics;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import static net.danh.bsoul.Manager.Data.savePlayerData;

public final class bSoul extends JavaPlugin {

    private static bSoul instance;

    public static bSoul getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        Metrics metrics = new Metrics(this, 12918);
        DCore.RegisterDCore(this);
        if (getServer().getPluginManager().getPlugin("MythicMobs") == null) {
            getServer().getPluginManager().registerEvents(new MobDeath(), this);
        }
        getServer().getPluginManager().registerEvents(new JoinQuit(), this);
        getServer().getPluginManager().registerEvents(new PlayerDeath(), this);
        getServer().getPluginManager().registerEvents(new SoulChange(), this);
        new Soul(this);
        if (getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new Placeholder().register();
        }
        Resources.createfiles();
        File.updateFile(this, Resources.getconfigfile(), "config.yml");
        File.updateFile(this, Resources.getlanguagefile(), "language.yml");
        (new BukkitRunnable() {
            public void run() {
                for (Player p : getServer().getOnlinePlayers()) {
                    if (Resources.getconfigfile().getBoolean("REHIBILITATE.ENABLE")) {
                        Data.addSoul(p, Resources.getconfigfile().getInt("REHIBILITATE.SOUL"));
                    }
                }
            }
        }).runTaskTimer(this, Resources.getconfigfile().getInt("REHIBILITATE.TIME") * 20L, Resources.getconfigfile().getInt("REHIBILITATE.TIME") * 20L);
    }

    @Override
    public void onDisable() {
        for (Player p : getServer().getOnlinePlayers()) {
            savePlayerData(p);
        }
        Resources.saveconfig();
        Resources.savelanguage();
        Resources.savemob();
    }
}
