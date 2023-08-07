package net.danh.bsoul.CustomEvents;

import net.danh.bsoul.Manager.Resources;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class SoulItemChangeEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private final Player p;
    private final int amount;
    private boolean cancel;

    public SoulItemChangeEvent(Player player, Integer count) {
        amount = count;
        p = player;
        cancel = !Resources.getconfigfile().getBoolean("ITEM.SOUL.ENABLE");
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public int getCount() {
        return amount;
    }

    public Player getPlayer() {
        return p;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    @Override
    public boolean isCancelled() {
        return cancel;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        cancel = cancelled;
    }
}
