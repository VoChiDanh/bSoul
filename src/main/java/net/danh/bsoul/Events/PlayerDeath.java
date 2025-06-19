package net.danh.bsoul.Events;

import net.danh.bsoul.Manager.Chat;
import net.danh.bsoul.Manager.Data;
import net.danh.bsoul.Manager.FileLoader;
import net.danh.bsoul.bSoul;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

import static net.danh.bsoul.Manager.Player.sendPlayerMessage;
import static net.danh.bsoul.Manager.Resources.getlanguagefile;

public class PlayerDeath implements Listener {

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onDeath(PlayerDeathEvent e) {
        Player p = e.getEntity();
        Player killer = p.getKiller();
        FileLoader fileLoader = new FileLoader();
        int soul_lose_amount = Math.max(1, fileLoader.getSoulLost());
        List<Integer> bls = fileLoader.getBlackListSlot();
        if (fileLoader.isSkipDeathScreen()) {
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
        if (fileLoader.isPvp()) {
            if (killer != null) {
                int soul = fileLoader.getKillSoul();
                double chance = fileLoader.getChance();
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
        if (fileLoader.isLoseItemWhenDeath() && !fileLoader.isLoseAllItem()) {
            int min = fileLoader.getMinSoulToLose();
            int lostAmount = Data.getLostAmount(p);
            if (Data.getSoul(p) <= min) {
                List<Integer> listSlot = new ArrayList<>();
                AtomicInteger atomicInteger = new AtomicInteger();
                PlayerInventory playerInventory = p.getInventory();
                int random = ThreadLocalRandom.current().nextInt(0, 100);
                for (int i = 0; i < playerInventory.getSize(); i++) {
                    if (playerInventory.getItem(i) != null && i == p.getInventory().getHeldItemSlot() && fileLoader.isPreventMainHand())
                        continue;
                    if (fileLoader.isSoulItemStatus()) {
                        if (i != fileLoader.getSoulSlot()) {
                            if (playerInventory.getItem(i) != null) {
                                if (!bls.isEmpty() && !bls.contains(i)) {
                                    if (!fileLoader.isMoreDrops())
                                        atomicInteger.set(i);
                                    else {
                                        listSlot.add(i);
                                        if (listSlot.size() == lostAmount)
                                            break;
                                        else continue;
                                    }
                                } else if (bls.isEmpty()) {
                                    if (!fileLoader.isMoreDrops())
                                        atomicInteger.set(i);
                                    else {
                                        listSlot.add(i);
                                        if (listSlot.size() == lostAmount)
                                            break;
                                        else continue;
                                    }
                                }
                                if (!fileLoader.isMoreDrops())
                                    break;
                            }
                        }
                    } else {
                        if (playerInventory.getItem(i) != null) {
                            if (!bls.contains(i)) {
                                if (!fileLoader.isMoreDrops()) {
                                    atomicInteger.set(i);
                                    break;
                                } else {
                                    listSlot.add(i);
                                    if (listSlot.size() == lostAmount)
                                        break;
                                }
                            }
                        }
                    }
                }
                if (!fileLoader.isMoreDrops()) {
                    int slot = atomicInteger.get();
                    ItemStack itemStack = playerInventory.getItem(slot);
                    if (itemStack != null) {
                        drop(p, slot, null);
                    }
                } else {
                    for (Integer integer : listSlot) drop(p, integer, null);
                }
                if (random <= 60 && random > 40 && fileLoader.isDropIncludeArmor()) {
                    int slot = ThreadLocalRandom.current().nextInt(0, 4);
                    ItemStack itemStack = null;
                    EquipmentSlot equipmentSlot = null;
                    for (ItemStack armor : playerInventory.getArmorContents()) {
                        if (armor != null && !armor.isEmpty()) {
                            if (slot == 3) {
                                itemStack = playerInventory.getItem(EquipmentSlot.FEET);
                                equipmentSlot = EquipmentSlot.FEET;
                            } else if (slot == 2) {
                                itemStack = playerInventory.getItem(EquipmentSlot.LEGS);
                                equipmentSlot = EquipmentSlot.LEGS;
                            } else if (slot == 1) {
                                itemStack = playerInventory.getItem(EquipmentSlot.CHEST);
                                equipmentSlot = EquipmentSlot.CHEST;
                            } else {
                                itemStack = playerInventory.getItem(EquipmentSlot.HEAD);
                                equipmentSlot = EquipmentSlot.HEAD;
                            }
                        }
                    }
                    if (itemStack != null)
                        drop(p, 0, equipmentSlot);
                } else if (random <= 90 && random > 60 && fileLoader.isDropIncludeOffhand()) {
                    if (!playerInventory.getItem(EquipmentSlot.OFF_HAND).isEmpty()) {
                        drop(p, 0, EquipmentSlot.OFF_HAND);
                    }
                }
            }
        } else if (fileLoader.isLoseItemWhenDeath() && !fileLoader.isMoreDrops()) {
            int min = fileLoader.getMinSoulToLose();
            PlayerInventory playerInventory = p.getInventory();
            if (Data.getSoul(p) <= min) {
                for (int i = 0; i < p.getInventory().getSize(); i++) {
                    if (p.getInventory().getItem(i) != null && i == p.getInventory().getHeldItemSlot() && fileLoader.isPreventMainHand())
                        continue;
                    if (fileLoader.isSoulItemStatus()) {
                        if (i != fileLoader.getSoulSlot()) {
                            if (playerInventory.getItem(i) != null) {
                                if (!bls.isEmpty() && !bls.contains(i)) {
                                    drop(p, i, null);
                                } else if (bls.isEmpty()) {
                                    drop(p, i, null);
                                }
                            }
                        }
                    } else {
                        if (playerInventory.getItem(i) != null) {
                            if (!bls.contains(i)) {
                                drop(p, i, null);
                            }
                        }
                    }
                }
                if (fileLoader.isDropIncludeArmor()) {
                    for (EquipmentSlot slot : EquipmentSlot.values()) {
                        drop(p, 0, slot);
                    }
                }
                if (fileLoader.isDropIncludeOffhand())
                    drop(p, 0, EquipmentSlot.OFF_HAND);
            }
        }
    }

    private void drop(Player p, int slot, @Nullable EquipmentSlot equipmentSlot) {
        PlayerInventory playerInventory = p.getInventory();
        FileLoader fileLoader = new FileLoader();
        String item;
        ItemStack itemStack;
        if (equipmentSlot == null)
            itemStack = playerInventory.getItem(slot);
        else itemStack = playerInventory.getItem(equipmentSlot);
        if (itemStack != null) {
            if (itemStack.getItemMeta() != null && itemStack.getItemMeta().hasDisplayName()) {
                item = itemStack.getItemMeta().getDisplayName();
            } else {
                item = itemStack.getType().name();
            }
            int amount;
            if (equipmentSlot == null)
                amount = Objects.requireNonNull(playerInventory.getItem(slot)).getAmount();
            else amount = playerInventory.getItem(equipmentSlot).getAmount();
            Chat.sendMessage(p, Chat.normalColorize(Objects.requireNonNull(getlanguagefile().getString("LOSE_ITEM")).replace("%item%", item).replace("%amount%", String.valueOf(amount))));
            if (fileLoader.isDropItem()) {
                World world = p.getLocation().getWorld();
                if (world != null) {
                    world.dropItem(p.getLocation(), itemStack);
                }
            }
            if (equipmentSlot == null)
                playerInventory.setItem(slot, null);
            else playerInventory.setItem(equipmentSlot, null);
        }
    }
}
