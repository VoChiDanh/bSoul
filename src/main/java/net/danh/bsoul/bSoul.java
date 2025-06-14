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
import net.danh.bsoul.Manager.FileLoader;
import net.danh.bsoul.Manager.Resources;
import net.danh.bsoul.Mythic.Register;
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
        getServer().getPluginManager().registerEvents(new MobDeath(), this);
        getServer().getPluginManager().registerEvents(new JoinQuit(), this);
        getServer().getPluginManager().registerEvents(new PlayerDeath(), this);
        new Soul();
        if (getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new Placeholder().register();
        }
        if (getServer().getPluginManager().getPlugin("MythicMobs") != null) {
            getServer().getPluginManager().registerEvents(new Register(), this);
        }
        Resources.createfiles();
        FileLoader fileLoader = new FileLoader();
        fileLoader.load();
        if (fileLoader.isSoulItemStatus()) {
            getServer().getPluginManager().registerEvents(new SoulChange(), this);
        }
        db = new SQLite(bSoul.getInstance());
        db.load();
        for (Player p : getServer().getOnlinePlayers()) {
            Data.setSoul(p, Data.getSoulData(p));
            Data.setSoulMax(p, Data.getMaxSoulData(p));
        }
        Debug.update27();
        Debug.update29();
        Debug.update295();
        Debug.update299();
        Debug.update300();
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> Bukkit.getServer().getOnlinePlayers().forEach(p -> {
            if (new FileLoader().isRehibilitate()) {
                if (getSoul(p) < getSoulMax(p)) {
                    Data.addSoul(p, new FileLoader().getSoul());
                }
            }
        }), new FileLoader().getTime() * 20L, new FileLoader().getTime() * 20L);
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
