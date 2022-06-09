package net.danh.bsoul.Events;

import net.danh.bsoul.CustomEvents.SoulChangeEvent;
import net.danh.bsoul.Manager.Item;
import net.danh.bsoul.Manager.Resources;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class SoulChange implements Listener {

    @EventHandler
    public void onSoulChange(SoulChangeEvent e) {
        Player p = e.getPlayer();
        Integer count = e.getCount();
        int slot = Resources.getconfigfile().getInt("ITEM.SOUL.SLOT");
        boolean enable = Resources.getconfigfile().getBoolean("ITEM.SOUL.ENABLE");
        if (enable) {
            p.getInventory().setItem(slot, Item.getSoulItems(count));
        }
    }
}
