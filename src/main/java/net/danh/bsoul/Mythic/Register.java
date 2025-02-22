package net.danh.bsoul.Mythic;

import io.lumine.mythic.bukkit.events.MythicMechanicLoadEvent;
import net.danh.bsoul.bSoul;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.logging.Level;

public class Register implements Listener {
    @EventHandler
    public void onMythicMechanicLoad(MythicMechanicLoadEvent event) {
        if (event.getMechanicName().equalsIgnoreCase("addSoul")) {
            event.register(new addSoulSkill(event.getConfig()));
            bSoul.getInstance().getLogger().log(Level.INFO, "-- Registered addSoul mechanic!");
        }
    }

}
