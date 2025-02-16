package net.danh.bsoul.Events;

import net.danh.bsoul.Manager.Data;
import org.bukkit.entity.Animals;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

import java.util.Objects;
import java.util.Random;

import static net.danh.bsoul.Manager.Debug.debug;
import static net.danh.bsoul.Manager.Player.sendPlayerMessage;
import static net.danh.bsoul.Manager.Resources.*;
import static net.danh.bsoul.Random.Number.getRandomInt;

public class MobDeath implements Listener {

    @EventHandler
    public void onKillVanillaMob(EntityDeathEvent e) {
        LivingEntity mob = e.getEntity();
        Player player = mob.getKiller();
        String vanillamobs = e.getEntityType().toString();
        if (player == null) {
            return;
        }
        if (!getconfigfile().getBoolean("MOBS.ENABLE")) {
            debug("MOBS.ENABLE doesn't enable");
            return;
        }
        if (mob instanceof Animals) {
            debug("Mobs is animals");
            if (!getconfigfile().getBoolean("MOBS.ANIMAL")) {
                debug("Mobs Animal doesn't enable");
                return;
            }
        }
        if (mob instanceof Monster) {
            debug("Mobs is Monster");
            if (!getconfigfile().getBoolean("MOBS.MONSTER")) {
                debug("Mobs monster doesn't enable");
                return;
            }
        }
        int max = getmobfile().getInt("VANILLA.DEFAULT.MAX");
        int min = getmobfile().getInt("VANILLA.DEFAULT.MIN");
        double chance = getmobfile().getDouble("VANILLA.DEFAULT.CHANCE");
        if (max == 0 && min == 0 && chance == 0.0) {
            return;
        }
        for (String getEntityType : Objects.requireNonNull(getmobfile().getConfigurationSection("VANILLA")).getKeys(false)) {
            if (getEntityType.equalsIgnoreCase(vanillamobs)) {
                max = getmobfile().getInt("VANILLA." + vanillamobs + ".MAX");
                min = getmobfile().getInt("VANILLA." + vanillamobs + ".MIN");
                chance = getmobfile().getDouble("VANILLA." + vanillamobs + ".CHANCE");
                break;
            }
        }
        debug("Max = " + max);
        debug("Min = " + min);
        debug("Chance = " + chance);
        int soul = getRandomInt(min, max);
        debug("Soul = " + soul);
        double real_chance = new Random().nextInt(100);
        debug("Real Chance = " + real_chance);
        if (chance >= real_chance) {
            if (Data.addSoul(player, soul)) {
                sendPlayerMessage(player, Objects.requireNonNull(getlanguagefile().getString("KILL_MOB")).replace("%soul%", String.format("%,d", soul)).replace("%mob%", mob.getName()));
            }
        }
    }
}
