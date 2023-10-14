package net.danh.bsoul;

import net.danh.bsoul.Cmd.Soul;
import net.danh.bsoul.Database.Database;
import net.danh.bsoul.Database.PlayerData;
import net.danh.bsoul.Database.SQLite;
import net.danh.bsoul.Events.JoinQuit;
import net.danh.bsoul.Events.MobDeath;
import net.danh.bsoul.Events.PlayerDeath;
import net.danh.bsoul.Events.SoulChange;
import net.danh.bsoul.Hook.Placeholder;
import net.danh.bsoul.Manager.Data;
import net.danh.bsoul.Manager.Debug;
import net.danh.bsoul.Manager.Resources;
import net.danh.dcore.DCore;
import net.danh.dcore.Utils.File;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import static net.danh.bsoul.Manager.Data.getSoul;
import static net.danh.bsoul.Manager.Data.getSoulMax;

public final class bSoul extends JavaPlugin {

    public static Database db;
    private static bSoul instance;

    public static bSoul getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        new Metrics(this, 12918);
        DCore.RegisterDCore(this);
        if (getServer().getPluginManager().getPlugin("bSoulMMAddon") == null) {
            getServer().getPluginManager().registerEvents(new MobDeath(), this);
        }
        getServer().getPluginManager().registerEvents(new JoinQuit(), this);
        getServer().getPluginManager().registerEvents(new PlayerDeath(), this);
        new Soul(this);
        if (getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new Placeholder().register();
        }
        Resources.createfiles();
        if (Resources.getconfigfile().getBoolean("ITEM.SOUL.ENABLE")) {
            getServer().getPluginManager().registerEvents(new SoulChange(), this);
        }
        File.updateFile(this, Resources.getconfigfile(), "config.yml");
        File.updateFile(this, Resources.getlanguagefile(), "language.yml");
        db = new SQLite(bSoul.getInstance());
        db.load();
        for (Player p : getServer().getOnlinePlayers()) {
            Data.setSoul(p, Data.getSoulData(p));
            Data.setSoulMax(p, Data.getMaxSoulData(p));
        }
        Debug.update27();
        Debug.update29();
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> Bukkit.getServer().getOnlinePlayers().forEach(p -> {
            if (Resources.getconfigfile().getBoolean("REHIBILITATE.ENABLE")) {
                if (getSoul(p) < getSoulMax(p)) {
                    Data.addSoul(p, Resources.getconfigfile().getInt("REHIBILITATE.SOUL"));
                }
            }
        }), Resources.getconfigfile().getLong("REHIBILITATE.TIME") * 20L, Resources.getconfigfile().getLong("REHIBILITATE.TIME") * 20L);
    }

    @Override
    public void onDisable() {
        for (Player p : getServer().getOnlinePlayers()) {
            bSoul.db.updateTable(new PlayerData(p.getName(), Data.getSoul(p), Data.getSoulMax(p)));
        }
        Resources.saveconfig();
        Resources.savelanguage();
        Resources.savemob();
    }
}
