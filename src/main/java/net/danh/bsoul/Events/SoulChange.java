package net.danh.bsoul.Events;

import net.danh.bsoul.CustomEvents.SoulItemChangeEvent;
import net.danh.bsoul.Manager.Item;
import net.danh.bsoul.Manager.Resources;
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
        if (Resources.getconfigfile().getBoolean("ITEM.SOUL.ENABLE")) {
            Material material = Material.getMaterial(Objects.requireNonNull(getconfigfile().getString("ITEM.SOUL.MATERIAL")));
            boolean unbreak = getconfigfile().getBoolean("ITEM.SOUL.UNBREAK");
            if (e.getItemDrop().getItemStack().getType() == material && Objects.requireNonNull(e.getItemDrop().getItemStack().getItemMeta()).isUnbreakable() == unbreak && e.getItemDrop().getItemStack().getAmount() == 1) {
                if (Resources.getconfigfile().getBoolean("ITEM.SOUL.ENABLE")) {
                    e.setCancelled(true);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onClick(InventoryClickEvent e) {
        if (Resources.getconfigfile().getBoolean("ITEM.SOUL.ENABLE")) {
            Material material = Material.getMaterial(Objects.requireNonNull(getconfigfile().getString("ITEM.SOUL.MATERIAL")));
            boolean unbreak = getconfigfile().getBoolean("ITEM.SOUL.UNBREAK");
            if (e.getWhoClicked() instanceof Player && e.getClickedInventory() != null) {
                if (e.getSlot() == Resources.getconfigfile().getInt("ITEM.SOUL.SLOT")) {
                    if (e.getCurrentItem() == null && e.getCurrentItem().getItemMeta() == null) {
                        return;
                    }
                    if (e.getCurrentItem().getType() == material && Objects.requireNonNull(e.getCurrentItem().getItemMeta()).isUnbreakable() == unbreak && e.getCurrentItem().getAmount() == 1) {
                        if (Resources.getconfigfile().getBoolean("ITEM.SOUL.ENABLE")) {
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
                        if (Resources.getconfigfile().getBoolean("ITEM.SOUL.ENABLE")) {
                            e.setCancelled(true);
                            e.setResult(Event.Result.DENY);
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onSoulChange(SoulItemChangeEvent e) {
        Player p = e.getPlayer();
        Integer count = e.getCount();
        int slot = Resources.getconfigfile().getInt("ITEM.SOUL.SLOT");
        if (!e.isCancelled()) {
            p.getInventory().setItem(slot, Item.getSoulItems(count));
        }
    }
}
