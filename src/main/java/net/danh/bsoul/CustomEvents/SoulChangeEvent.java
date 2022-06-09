package net.danh.bsoul.CustomEvents;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class SoulChangeEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private final Player p;
    private final int amount;
    private boolean cancelled;

    public SoulChangeEvent(Player player, Integer count) {
        amount = count;
        p = player;
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
}
