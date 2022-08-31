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
import net.danh.bsoul.Manager.Resources;
import net.danh.dcore.DCore;
import net.danh.dcore.Utils.File;
import org.bstats.bukkit.Metrics;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import static net.danh.bsoul.Manager.Data.getSoul;
import static net.danh.bsoul.Manager.Data.getSoulMax;

public final class bSoul extends JavaPlugin {

    private static final boolean SOUL_ITEM = Resources.getconfigfile().getBoolean("ITEM.SOUL.ENABLE");
    public static Database db;
    private static bSoul instance;

    public static bSoul getInstance() {
        return instance;
    }

    public static boolean isSoulItem() {
        return SOUL_ITEM;
    }

    @Override
    public void onEnable() {
        instance = this;
        Metrics metrics = new Metrics(this, 12918);
        DCore.RegisterDCore(this);
        if (getServer().getPluginManager().getPlugin("bSoulMMAddon") == null) {
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
        db = new SQLite(bSoul.getInstance());
        db.load();
        for (Player p : getServer().getOnlinePlayers()) {
            Data.setSoul(p, Data.getSoulData(p));
            Data.setSoulMax(p, Data.getMaxSoulData(p));
        }
        (new BukkitRunnable() {
            public void run() {
                for (Player p : getServer().getOnlinePlayers()) {
                    if (Resources.getconfigfile().getBoolean("REHIBILITATE.ENABLE")) {
                        if (getSoul(p) < getSoulMax(p)) {
                            Data.addSoul(p, Resources.getconfigfile().getInt("REHIBILITATE.SOUL"));
                        }
                    }
                }
            }
        }).runTaskTimer(this, Resources.getconfigfile().getInt("REHIBILITATE.TIME") * 20L, Resources.getconfigfile().getInt("REHIBILITATE.TIME") * 20L);
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
