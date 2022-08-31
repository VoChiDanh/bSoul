package net.danh.bsoul.Events;

import net.danh.bsoul.CustomEvents.SoulChangeEvent;
import net.danh.bsoul.Manager.Item;
import net.danh.bsoul.Manager.Resources;
import net.danh.bsoul.bSoul;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

import static net.danh.bsoul.Manager.Resources.getconfigfile;

public class SoulChange implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onClick(PlayerDropItemEvent e) {
        Material material = Material.getMaterial(Objects.requireNonNull(getconfigfile().getString("ITEM.SOUL.MATERIAL")));
        boolean unbreak = getconfigfile().getBoolean("ITEM.SOUL.UNBREAK");
        if (e.getItemDrop().getItemStack().getType() == material && Objects.requireNonNull(e.getItemDrop().getItemStack().getItemMeta()).isUnbreakable() == unbreak && e.getItemDrop().getItemStack().getAmount() == 1) {
            if (bSoul.isSoulItem()) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onClick(InventoryClickEvent e) {
        Material material = Material.getMaterial(Objects.requireNonNull(getconfigfile().getString("ITEM.SOUL.MATERIAL")));
        boolean unbreak = getconfigfile().getBoolean("ITEM.SOUL.UNBREAK");
        if (e.getWhoClicked() instanceof Player && e.getClickedInventory() != null) {
            if (e.getSlot() == Resources.getconfigfile().getInt("ITEM.SOUL.SLOT")) {
                if (e.getCurrentItem() == null && e.getCurrentItem().getItemMeta() == null) {
                    return;
                }
                if (e.getCurrentItem().getType() == material && Objects.requireNonNull(e.getCurrentItem().getItemMeta()).isUnbreakable() == unbreak && e.getCurrentItem().getAmount() == 1) {
                    if (bSoul.isSoulItem()) {
                        e.setCancelled(true);
                        e.setResult(Event.Result.DENY);
                    }
                }
            }
            if (e.getClick() == ClickType.NUMBER_KEY) {
                ItemStack item = (e.getClick() == org.bukkit.event.inventory.ClickType.NUMBER_KEY) ? e.getWhoClicked().getInventory().getItem(e.getHotbarButton()) : e.getCurrentItem();
                if (item == null) {
                    return;
                }
                if (item.getItemMeta() == null) {
                    return;
                }
                if (item.getType() == material && item.getItemMeta().isUnbreakable() == unbreak && item.getAmount() == 1) {
                    if (bSoul.isSoulItem()) {
                        e.setCancelled(true);
                        e.setResult(Event.Result.DENY);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onSoulChange(SoulChangeEvent e) {
        Player p = e.getPlayer();
        Integer count = e.getCount();
        int slot = Resources.getconfigfile().getInt("ITEM.SOUL.SLOT");
        boolean enable = Resources.getconfigfile().getBoolean("ITEM.SOUL.ENABLE");
        if (enable && bSoul.isSoulItem()) {
            p.getInventory().setItem(slot, Item.getSoulItems(count));
        }
    }
}
