package net.danh.bsoul.Events;

import net.danh.bsoul.Manager.Data;
import net.danh.bsoul.Manager.Resources;
import net.danh.bsoul.bSoul;
import net.danh.dcore.Utils.Chat;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.PlayerInventory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import static net.danh.bsoul.Manager.Resources.getconfigfile;
import static net.danh.bsoul.Manager.Resources.getlanguagefile;
import static net.danh.dcore.Random.Number.getRandomInt;
import static net.danh.dcore.Utils.Player.sendPlayerMessage;

public class PlayerDeath implements Listener {

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOW)
    public void onDeath(PlayerDeathEvent e) {
        Player p = e.getEntity();
        Player killer = p.getKiller();
        int soul_lose_amount = getconfigfile().getInt("DEATH.SOUL_LOST");
        List<Integer> bls = Resources.getconfigfile().getIntegerList("SETTINGS.BLACKLIST_SLOTS");
        if (getconfigfile().getBoolean("DEATH.SKIP_DEATH_SCREEN")) {
            Bukkit.getScheduler().scheduleSyncDelayedTask(bSoul.getInstance(), () -> p.spigot().respawn(), 2);
        }
        Data.removeSoul(p, soul_lose_amount);
        sendPlayerMessage(p, Objects.requireNonNull(getlanguagefile().getString("DEAD_MESSAGE")).replace("%amount%", String.valueOf(soul_lose_amount)).replace("%left%", String.valueOf(Data.getSoul(p))));
        if (getconfigfile().getBoolean("PVP.ENABLE")) {
            if (killer != null) {
                int soul = getconfigfile().getInt("PVP.KILL_SOUL");
                double chance = getconfigfile().getDouble("PVP.CHANCE");
                double real_chance = new Random().nextInt(100);
                if (chance >= real_chance) {
                    Data.addSoul(killer, soul);
                    sendPlayerMessage(killer, Objects.requireNonNull(getlanguagefile().getString("KILL_PLAYER_MESSAGE")).replace("%player%", p.getName()).replace("%amount%", String.valueOf(soul_lose_amount)));
                }
            }
        }
        if (getconfigfile().getBoolean("DEATH.LOSE_ITEM_WHEN_DEATH") && !getconfigfile().getBoolean("DEATH.LOSE_ALL_ITEM")) {
            int min = getconfigfile().getInt("DEATH.MIN_SOUL_TO_LOSE");
            if (Data.getSoul(p) <= min) {
                List<Integer> fullSlots = new ArrayList<>();
                PlayerInventory playerInventory = p.getInventory();
                for (int i = 1; i < playerInventory.getSize(); i++) {
                    if (Resources.getconfigfile().contains("SETTINGS.BLACKLIST_SLOTS") && !bls.isEmpty()) {
                        if (getconfigfile().getBoolean("ITEM.SOUL.ENABLE")) {
                            if (i != getconfigfile().getInt("ITEM.SOUL.SLOT")) {
                                if (playerInventory.getItem(i) != null) {
                                    if (!bls.contains(i)) {
                                        fullSlots.add(i);
                                    }
                                }
                            }
                        } else {
                            if (playerInventory.getItem(i) != null) {
                                fullSlots.add(i);
                            }
                        }
                    } else {
                        if (getconfigfile().getBoolean("ITEM.SOUL.ENABLE")) {
                            if (i != getconfigfile().getInt("ITEM.SOUL.SLOT")) {
                                if (playerInventory.getItem(i) != null) {
                                    fullSlots.add(i);
                                }
                            }
                        } else {
                            if (playerInventory.getItem(i) != null) {
                                fullSlots.add(i);
                            }
                        }
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
                        sendPlayerMessage(p, Chat.colorize(Objects.requireNonNull(getlanguagefile().getString("LOSE_ITEM")).replaceAll("%item%", item).replaceAll("%amount%", String.valueOf(amount))));
                        playerInventory.setItem(slot, null);
                    }
                }
            }
        } else if (getconfigfile().getBoolean("DEATH.LOSE_ITEM_WHEN_DEATH") && getconfigfile().getBoolean("DEATH.LOSE_ALL_ITEM")) {
            int min = getconfigfile().getInt("DEATH.MIN_SOUL_TO_LOSE");
            if (Data.getSoul(p) <= min) {
                for (int i = 1; i <= p.getInventory().getSize(); i++) {
                    if (getconfigfile().getBoolean("ITEM.SOUL.ENABLE")) {
                        if (i != getconfigfile().getInt("ITEM.SOUL.SLOT")) {
                            p.getInventory().setItem(i, null);
                        }
                    } else {
                        p.getInventory().setItem(i, null);
                    }
                }
            }
        }
    }
}
