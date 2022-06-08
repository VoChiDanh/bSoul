package net.danh.bsoul.Hook;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.danh.bsoul.Manager.Data;
import net.danh.bsoul.bSoul;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Placeholder extends PlaceholderExpansion {

    @Override
    public @NotNull String getIdentifier() {
        return "bsoul";
    }

    @Override
    public @NotNull String getAuthor() {
        return bSoul.getInstance().getDescription().getAuthors().toString();
    }

    @Override
    public @NotNull String getVersion() {
        return bSoul.getInstance().getDescription().getVersion();
    }

    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public @Nullable String onPlaceholderRequest(Player p, @NotNull String params) {
        if (p == null) return "null";
        if (params.equalsIgnoreCase("soul")) {
            return String.valueOf(Data.getSoul(p));
        }
        if (params.equalsIgnoreCase("max_soul")) {
            return String.valueOf(Data.getSoulMax(p));
        }
        return null;
    }
}


