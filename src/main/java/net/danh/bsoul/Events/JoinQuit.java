package net.danh.bsoul.Events;

import net.danh.bsoul.Manager.Data;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class JoinQuit implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        Data.setSoul(p, Data.getSoulData(p));
        Data.setSoulMax(p, Data.getMaxSoulData(p));
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        Data.savePlayerData(p);
    }
}
