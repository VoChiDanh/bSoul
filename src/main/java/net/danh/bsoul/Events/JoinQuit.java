package net.danh.bsoul.Events;

import net.danh.bsoul.Manager.Data;
import net.danh.bsoul.bSoul;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Objects;

import static net.danh.bsoul.Manager.Item.getSoulItems;

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
        p.getInventory().remove(Objects.requireNonNull(getSoulItems(Data.getSoul(p))));
        bSoul.db.save(p);
    }
}
