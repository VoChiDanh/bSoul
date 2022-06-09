package net.danh.bsoul.Events;

import net.danh.bsoul.Manager.Data;
import net.danh.bsoul.bSoul;
import net.danh.dcore.Utils.Chat;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.PlayerInventory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static net.danh.bsoul.Manager.Resources.getconfigfile;
import static net.danh.bsoul.Manager.Resources.getlanguagefile;
import static net.danh.dcore.Random.Number.getRandomInt;
import static net.danh.dcore.Utils.Player.sendPlayerMessage;

public class PlayerDeath implements Listener {

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        Player p = e.getEntity();
        Player killer = p.getKiller();
        if (getconfigfile().getBoolean("DEATH.SKIP_DEATH_SCREEN")) {
            Bukkit.getScheduler().scheduleSyncDelayedTask(bSoul.getInstance(), () -> p.spigot().respawn(), 2);
        }
        Data.removeSoul(p, 1);
        if (getconfigfile().getBoolean("PVP.ENABLE")) {
            if (killer != null) {
                int soul = getconfigfile().getInt("PVP.KILL_SOUL");
                double chance = getconfigfile().getDouble("PVP.CHANCE");
                double real_chance = Math.random() * 100.0D;
                if (chance >= real_chance) {
                    Data.addSoul(killer, soul);
                }
            }
        }
        if (getconfigfile().getBoolean("DEATH.LOSE_ITEM_WHEN_DEATH")) {
            int min = getconfigfile().getInt("DEATH.MIN_SOUL_TO_LOSE");
            if (Data.getSoul(p) <= min) {
                List<Integer> fullSlots = new ArrayList<>();
                PlayerInventory playerInventory = p.getInventory();
                for (int i = 1; i <= playerInventory.getSize(); i++) {
                    if (playerInventory.getItem(i) != null) {
                        fullSlots.add(i);
                    }
                }
                if (fullSlots.size() == 0) {
                    return;
                }
                int slot = getRandomInt(1, fullSlots.size());
                if (playerInventory.getItem(slot) != null) {
                    if (Objects.requireNonNull(playerInventory.getItem(slot)).getItemMeta() != null) {
                        String item = Objects.requireNonNull(Objects.requireNonNull(playerInventory.getItem(slot)).getItemMeta()).getDisplayName();
                        int amount = Objects.requireNonNull(playerInventory.getItem(slot)).getAmount();
                        sendPlayerMessage(p, getlanguagefile().getString(Chat.colorize("LOSE_ITEM").replaceAll("%item%", item).replaceAll("%amount%", String.valueOf(amount))));
                        playerInventory.setItem(slot, null);
                    }
                }
            }
        }
    }
}
