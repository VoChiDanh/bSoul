package net.danh.bsoul.Events;

import net.danh.bsoul.Manager.Chat;
import net.danh.bsoul.Manager.Data;
import net.danh.bsoul.Manager.Resources;
import net.danh.bsoul.bSoul;
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

import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import static net.danh.bsoul.Manager.Player.sendPlayerMessage;
import static net.danh.bsoul.Manager.Resources.getconfigfile;
import static net.danh.bsoul.Manager.Resources.getlanguagefile;

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
                Chat.sendMessage(killer, Chat.normalColorize(Objects.requireNonNull(getlanguagefile().getString("KILL_PLAYER_WITHOUT_SOUL"))));
            }
        }
        Chat.sendMessage(p, Objects.requireNonNull(getlanguagefile().getString("DEAD_MESSAGE")).replace("%amount%", String.valueOf(soul_lose_amount)).replace("%left%", String.valueOf(Data.getSoul(p))));
        if (getconfigfile().getBoolean("PVP.ENABLE")) {
            if (killer != null) {
                int soul = getconfigfile().getInt("PVP.KILL_SOUL");
                double chance = getconfigfile().getDouble("PVP.CHANCE");
                double real_chance = new Random().nextInt(100);
                if (chance >= real_chance) {
                    if (Data.getSoul(p) > 0) {
                        if (Data.addSoul(killer, soul)) {
                            sendPlayerMessage(killer, Objects.requireNonNull(getlanguagefile().getString("KILL_PLAYER_MESSAGE")).replace("%player%", p.getName()).replace("%amount%", String.valueOf(soul_lose_amount)));
                        }
                    }
                }
            }
        }
        if (getconfigfile().getBoolean("DEATH.LOSE_ITEM_WHEN_DEATH") && !getconfigfile().getBoolean("DEATH.LOSE_ALL_ITEM")) {
            int min = getconfigfile().getInt("DEATH.MIN_SOUL_TO_LOSE");
            if (Data.getSoul(p) <= min) {
                AtomicInteger atomicInteger = new AtomicInteger();
                PlayerInventory playerInventory = p.getInventory();
                for (int i = 0; i < playerInventory.getSize(); i++) {
                    if (playerInventory.getItem(i) != null && playerInventory.getItem(i) == p.getInventory().getItemInMainHand() && getconfigfile().getBoolean("DEATH.PREVENT_MAIN_HAND"))
                        continue;
                    if (Resources.getconfigfile().contains("SETTINGS.BLACKLIST_SLOTS") && !bls.isEmpty()) {
                        if (getconfigfile().getBoolean("ITEM.SOUL.ENABLE")) {
                            if (i != getconfigfile().getInt("ITEM.SOUL.SLOT")) {
                                if (playerInventory.getItem(i) != null) {
                                    if (!bls.contains(i)) {
                                        atomicInteger.set(i);
                                        break;
                                    }
                                }
                            }
                        } else {
                            if (playerInventory.getItem(i) != null) {
                                if (!bls.contains(i)) {
                                    atomicInteger.set(i);
                                    break;
                                }
                            }
                        }
                    } else {
                        if (getconfigfile().getBoolean("ITEM.SOUL.ENABLE")) {
                            if (i != getconfigfile().getInt("ITEM.SOUL.SLOT")) {
                                if (playerInventory.getItem(i) != null) {
                                    atomicInteger.set(i);
                                    break;
                                }
                            }
                        } else {
                            if (playerInventory.getItem(i) != null) {
                                atomicInteger.set(i);
                                break;
                            }
                        }
                    }
                }
                int slot = atomicInteger.get();
                ItemStack itemStack = playerInventory.getItem(slot);
                if (itemStack != null) {
                    String item;
                    if (itemStack.getItemMeta() != null && itemStack.getItemMeta().hasDisplayName()) {
                        item = itemStack.getItemMeta().getDisplayName();
                    } else {
                        item = itemStack.getType().name();
                    }
                    int amount = Objects.requireNonNull(playerInventory.getItem(slot)).getAmount();
                    Chat.sendMessage(p, Chat.normalColorize(Objects.requireNonNull(getlanguagefile().getString("LOSE_ITEM")).replace("%item%", item).replace("%amount%", String.valueOf(amount))));
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
                for (int i = 0; i < p.getInventory().getSize(); i++) {
                    if (p.getInventory().getItem(i) != null && p.getInventory().getItem(i) == p.getInventory().getItemInMainHand() && getconfigfile().getBoolean("DEATH.PREVENT_MAIN_HAND"))
                        continue;
                    if (getconfigfile().getBoolean("ITEM.SOUL.ENABLE")) {
                        if (i != getconfigfile().getInt("ITEM.SOUL.SLOT")) {
                            ItemStack itemStack = p.getInventory().getItem(i);
                            if (Resources.getconfigfile().contains("SETTINGS.BLACKLIST_SLOTS") && !bls.isEmpty()) {
                                if (!bls.contains(i)) {
                                    if (itemStack != null && itemStack.getType() != Material.AIR) {
                                        if (getconfigfile().getBoolean("DEATH.DROP_ITEM")) {
                                            World world = p.getLocation().getWorld();
                                            if (world != null) {
                                                world.dropItem(p.getLocation(), itemStack);
                                            }
                                        }
                                        p.getInventory().setItem(i, null);
                                    }
                                }
                            } else {
                                if (itemStack != null && itemStack.getType() != Material.AIR) {
                                    if (getconfigfile().getBoolean("DEATH.DROP_ITEM")) {
                                        World world = p.getLocation().getWorld();
                                        if (world != null) {
                                            world.dropItem(p.getLocation(), itemStack);
                                        }
                                    }
                                    p.getInventory().setItem(i, null);
                                }
                            }
                        }
                    } else {
                        ItemStack itemStack = p.getInventory().getItem(i);
                        if (Resources.getconfigfile().contains("SETTINGS.BLACKLIST_SLOTS") && !bls.isEmpty()) {
                            if (!bls.contains(i)) {
                                if (itemStack != null && itemStack.getType() != Material.AIR) {
                                    if (getconfigfile().getBoolean("DEATH.DROP_ITEM")) {
                                        World world = p.getLocation().getWorld();
                                        if (world != null) {
                                            world.dropItem(p.getLocation(), itemStack);
                                        }
                                    }
                                    p.getInventory().setItem(i, null);
                                }
                            }
                        } else {
                            if (itemStack != null && itemStack.getType() != Material.AIR) {
                                if (getconfigfile().getBoolean("DEATH.DROP_ITEM")) {
                                    World world = p.getLocation().getWorld();
                                    if (world != null) {
                                        world.dropItem(p.getLocation(), itemStack);
                                    }
                                }
                                p.getInventory().setItem(i, null);
                            }
                        }
                    }
                }
            }
        }
    }
}
