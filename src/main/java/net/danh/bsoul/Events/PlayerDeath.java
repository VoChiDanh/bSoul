package net.danh.bsoul.Events;

import net.danh.bsoul.Manager.Data;
import net.danh.bsoul.Manager.Resources;
import net.danh.bsoul.bSoul;
import net.danh.dcore.Utils.Chat;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
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

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onDeath(PlayerDeathEvent e) {
        Player p = e.getEntity();
        Player killer = p.getKiller();
        int soul_lose_amount = Math.max(1, getconfigfile().getInt("DEATH.SOUL_LOST"));
        List<Integer> bls = Resources.getconfigfile().getIntegerList("SETTINGS.BLACKLIST_SLOTS");
        if (getconfigfile().getBoolean("DEATH.SKIP_DEATH_SCREEN")) {
            Bukkit.getScheduler().scheduleSyncDelayedTask(bSoul.getInstance(), () -> p.spigot().respawn(), 2);
        }
        if (Data.getSoul(p) > 0) {
            Data.removeSoul(p, soul_lose_amount);
        } else {
            if (killer != null) {
                killer.sendMessage(Chat.colorize(Objects.requireNonNull(getlanguagefile().getString("KILL_PLAYER_WITHOUT_SOUL"))));
            }
        }
        sendPlayerMessage(p, Objects.requireNonNull(getlanguagefile().getString("DEAD_MESSAGE")).replace("%amount%", String.valueOf(soul_lose_amount)).replace("%left%", String.valueOf(Data.getSoul(p))));
        if (getconfigfile().getBoolean("PVP.ENABLE")) {
            if (killer != null) {
                int soul = getconfigfile().getInt("PVP.KILL_SOUL");
                double chance = getconfigfile().getDouble("PVP.CHANCE");
                double real_chance = new Random().nextInt(100);
                if (chance >= real_chance) {
                    if (Data.getSoul(p) > 0) {
                        Data.addSoul(killer, soul);
                        sendPlayerMessage(killer, Objects.requireNonNull(getlanguagefile().getString("KILL_PLAYER_MESSAGE")).replace("%player%", p.getName()).replace("%amount%", String.valueOf(soul_lose_amount)));
                    }
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
                if (fullSlots.isEmpty()) {
                    return;
                }
                int slot = getRandomInt(1, fullSlots.size());
                ItemStack itemStack = playerInventory.getItem(slot);
                if (itemStack != null) {
                    String item;
                    if (itemStack.getItemMeta() != null && itemStack.getItemMeta().hasDisplayName()) {
                        item = itemStack.getItemMeta().getDisplayName();
                    } else {
                        item = itemStack.getType().name();
                    }
                    int amount = Objects.requireNonNull(playerInventory.getItem(slot)).getAmount();
                    sendPlayerMessage(p, Chat.colorize(Objects.requireNonNull(getlanguagefile().getString("LOSE_ITEM")).replaceAll("%item%", item).replaceAll("%amount%", String.valueOf(amount))));
                    if (getconfigfile().getBoolean("DEATH.DROP_ITEM")) {
                        World world = p.getLocation().getWorld();
                        if (world != null) {
                            world.dropItem(p.getLocation(), itemStack);
                        }
                    }
                    playerInventory.setItem(slot, null);
                }
            }
        } else if (getconfigfile().getBoolean("DEATH.LOSE_ITEM_WHEN_DEATH") && getconfigfile().getBoolean("DEATH.LOSE_ALL_ITEM")) {
            int min = getconfigfile().getInt("DEATH.MIN_SOUL_TO_LOSE");
            if (Data.getSoul(p) <= min) {
                for (int i = 1; i <= p.getInventory().getSize(); i++) {
                    if (getconfigfile().getBoolean("ITEM.SOUL.ENABLE")) {
                        if (i != getconfigfile().getInt("ITEM.SOUL.SLOT")) {
                            ItemStack itemStack = p.getInventory().getItem(i);
                            if (itemStack != null && itemStack.getType() != Material.AIR) {
                                if (getconfigfile().getBoolean("DEATH.DROP_ITEM")) {
                                    World world = p.getLocation().getWorld();
                                    if (world != null) {
                                        world.dropItem(p.getLocation(), itemStack);
                                    }
                                }
                            }
                            p.getInventory().setItem(i, null);
                        }
                    } else {
                        ItemStack itemStack = p.getInventory().getItem(i);
                        if (itemStack != null && itemStack.getType() != Material.AIR) {
                            if (getconfigfile().getBoolean("DEATH.DROP_ITEM")) {
                                World world = p.getLocation().getWorld();
                                if (world != null) {
                                    world.dropItem(p.getLocation(), itemStack);
                                }
                            }
                        }
                        p.getInventory().setItem(i, null);

                    }
                }
            }
        }
    }
}
