package net.danh.bsoul.Commands;

import net.danh.bsoul.bSoul;
import org.bukkit.command.*;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

/**
 * @version 1.0
 */
public abstract class CMDBase implements CommandExecutor, TabCompleter {

    public CMDBase(String name) {
        PluginCommand pluginCommand = bSoul.getInstance().getCommand(name);
        Objects.requireNonNull(pluginCommand).setExecutor(this);
        pluginCommand.setTabCompleter(this);
    }

    public abstract void execute(CommandSender c, String[] args);

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        execute(sender, args);
        return true;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        return TabComplete(sender, args);
    }

    public abstract List<String> TabComplete(CommandSender sender, String[] args);
}

