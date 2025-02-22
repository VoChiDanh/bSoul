package net.danh.bsoul.Cmd;

import net.danh.bsoul.Commands.CMDBase;
import net.danh.bsoul.Manager.Data;
import net.danh.bsoul.Manager.Resources;
import net.danh.bsoul.Random.Number;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.StringUtil;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static net.danh.bsoul.Manager.Player.sendConsoleMessage;
import static net.danh.bsoul.Manager.Player.sendPlayerMessage;
import static net.danh.bsoul.Manager.Resources.getlanguagefile;
import static net.danh.bsoul.Random.Number.getInt;

public class Soul extends CMDBase {

    public Soul(JavaPlugin core) {
        super(core, "soul");
    }

    @Override
    public void playerexecute(Player p, String[] args) {
        if (args.length == 0) {
            sendPlayerMessage(p, Objects.requireNonNull(getlanguagefile().getString("YOUR_SOUL")).replace("%soul%", String.valueOf(Data.getSoul(p))));
        }

        if (p.hasPermission("soul.transfer")) {
            if (args.length == 3) {
                if (args[0].equalsIgnoreCase("transfer")) {
                    Player t = Bukkit.getPlayer(args[1]);
                    if (t != null && p != t) {
                        int amount = Number.getInt(args[2]);
                        if (Data.getSoul(p) - amount >= 0) {
                            Data.addSoul(t, amount);
                            sendPlayerMessage(t, Objects.requireNonNull(getlanguagefile().getString("CHANGE_SOUL")).replace("%change%", "&a+").replace("%soul%", new DecimalFormat("###,###.###").format(amount)).replace("%player%", p.getName()));
                            Data.removeSoul(p, amount);
                            sendPlayerMessage(p, Objects.requireNonNull(getlanguagefile().getString("CHANGE_SOUL")).replace("%change%", "&c-").replace("%soul%", new DecimalFormat("###,###.###").format(amount)).replace("%player%", t.getName()));
                        }
                    }
                }
            }
        }
        if (p.hasPermission("soul.user")) {
            if (args.length == 2) {
                if (args[0].equalsIgnoreCase("check")) {
                    Player target = Bukkit.getPlayer(args[1]);
                    if (target == null) {
                        sendPlayerMessage(p, "&cPlayer is not online");
                        return;
                    }
                    int soul = Data.getSoul(target);
                    sendPlayerMessage(p, Objects.requireNonNull(getlanguagefile().getString("CHECK_SOUL")).replace("%player%", target.getName()).replace("%soul%", new DecimalFormat("###,###.###").format(soul)));
                }
            }
        }
        if (p.hasPermission("soul.admin")) {
            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("help")) {
                    sendPlayerMessage(p, getlanguagefile().getStringList("HELP.PLAYER"));
                    if (p.hasPermission("soul.admin")) {
                        sendPlayerMessage(p, getlanguagefile().getStringList("HELP.ADMIN"));
                    }
                }
                if (args[0].equalsIgnoreCase("reload")) {
                    Resources.reloadfiles();
                    sendPlayerMessage(p, "&aReloaded");
                }
            }
            if (args.length == 4 || args.length == 5) {
                if (args[0].equalsIgnoreCase("add") || args[0].equalsIgnoreCase("set") || args[0].equalsIgnoreCase("remove")) {
                    Player target = Bukkit.getPlayer(args[2]);
                    if (target == null) {
                        sendPlayerMessage(p, "&cPlayer is null");
                        return;
                    }
                    if (getInt(args[3]) > 0) {
                        Integer soul = Number.getInt(args[3]);
                        if (args[0].equalsIgnoreCase("add")) {
                            if (args[1].equalsIgnoreCase("soul")) {
                                if (Data.addSoul(target, soul) && args.length == 4) {
                                    sendPlayerMessage(p, Objects.requireNonNull(getlanguagefile().getString("CHANGE_SOUL")).replace("%change%", "&a+").replace("%soul%", new DecimalFormat("###,###.###").format(soul)).replace("%player%", target.getName()));
                                }
                            }
                            if (args[1].equalsIgnoreCase("max")) {
                                Data.addSoulMax(target, soul);
                                sendPlayerMessage(p, Objects.requireNonNull(getlanguagefile().getString("CHANGE_MAX_SOUL")).replace("%change%", "&a+").replace("%soul%", new DecimalFormat("###,###.###").format(soul)).replace("%player%", target.getName()));
                            }
                        }
                        if (args[0].equalsIgnoreCase("remove")) {
                            if (args[1].equalsIgnoreCase("soul")) {
                                Data.removeSoul(target, soul);
                                sendPlayerMessage(p, Objects.requireNonNull(getlanguagefile().getString("CHANGE_SOUL")).replace("%change%", "&c-").replace("%soul%", new DecimalFormat("###,###.###").format(soul)).replace("%player%", target.getName()));
                            }
                            if (args[1].equalsIgnoreCase("max")) {
                                Data.removeSoulMax(target, soul);
                                sendPlayerMessage(p, Objects.requireNonNull(getlanguagefile().getString("CHANGE_MAX_SOUL")).replace("%change%", "&c-").replace("%soul%", new DecimalFormat("###,###.###").format(soul)).replace("%player%", target.getName()));
                            }
                        }
                        if (args[0].equalsIgnoreCase("set")) {
                            if (args[1].equalsIgnoreCase("soul")) {
                                Data.setSoul(target, soul);
                                sendPlayerMessage(p, Objects.requireNonNull(getlanguagefile().getString("CHECK_SOUL")).replace("%player%", target.getName()).replace("%soul%", new DecimalFormat("###,###.###").format(soul)));
                            }
                            if (args[1].equalsIgnoreCase("max")) {
                                Data.setSoulMax(target, soul);
                                sendPlayerMessage(p, Objects.requireNonNull(getlanguagefile().getString("CHECK_MAX_SOUL")).replace("%player%", target.getName()).replace("%soul%", new DecimalFormat("###,###.###").format(soul)));
                            }
                        }
                    } else {
                        sendPlayerMessage(p, "&cSomething is go wrong....");
                    }
                }
            }
        }
    }

    @Override
    public void consoleexecute(ConsoleCommandSender c, String[] args) {
        if (args.length == 0) {
            sendConsoleMessage(c, getlanguagefile().getStringList("HELP.PLAYER"));
            sendConsoleMessage(c, getlanguagefile().getStringList("HELP.ADMIN"));
        }
        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("check")) {
                Player target = Bukkit.getPlayer(args[1]);
                if (target == null) {
                    sendConsoleMessage(c, "&cPlayer is not online");
                    return;
                }
                int soul = Data.getSoul(target);
                sendConsoleMessage(c, Objects.requireNonNull(getlanguagefile().getString("CHECK_SOUL")).replace("%player%", target.getName()).replace("%soul%", new DecimalFormat("###,###.###").format(soul)));
            }
        }
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("reload")) {
                Resources.reloadfiles();
                sendConsoleMessage(c, "&aReloaded");
            }
        }
        if (args.length == 4) {
            if (args[0].equalsIgnoreCase("add") || args[0].equalsIgnoreCase("set") || args[0].equalsIgnoreCase("remove")) {
                Player target = Bukkit.getPlayer(args[2]);
                if (target == null) {
                    sendConsoleMessage(c, "&cPlayer is null");
                    return;
                }
                if (getInt(args[3]) > 0) {
                    Integer soul = Integer.parseInt(args[3]);
                    if (args[0].equalsIgnoreCase("add")) {
                        if (args[1].equalsIgnoreCase("soul")) {
                            if (Data.addSoul(target, soul)) {
                                sendConsoleMessage(c, Objects.requireNonNull(getlanguagefile().getString("CHANGE_SOUL")).replace("%change%", "&a+").replace("%soul%", new DecimalFormat("###,###.###").format(soul)).replace("%player%", target.getName()));
                            }
                        }
                        if (args[1].equalsIgnoreCase("max")) {
                            Data.addSoulMax(target, soul);
                            sendConsoleMessage(c, Objects.requireNonNull(getlanguagefile().getString("CHANGE_MAX_SOUL")).replace("%change%", "&a+").replace("%soul%", new DecimalFormat("###,###.###").format(soul)).replace("%player%", target.getName()));
                        }
                    }
                    if (args[0].equalsIgnoreCase("remove")) {
                        if (args[1].equalsIgnoreCase("soul")) {
                            Data.removeSoul(target, soul);
                            sendConsoleMessage(c, Objects.requireNonNull(getlanguagefile().getString("CHANGE_SOUL")).replace("%change%", "&c-").replace("%soul%", new DecimalFormat("###,###.###").format(soul)).replace("%player%", target.getName()));
                        }
                        if (args[1].equalsIgnoreCase("max")) {
                            Data.removeSoulMax(target, soul);
                            sendConsoleMessage(c, Objects.requireNonNull(getlanguagefile().getString("CHANGE_MAX_SOUL")).replace("%change%", "&c-").replace("%soul%", new DecimalFormat("###,###.###").format(soul)).replace("%player%", target.getName()));
                        }
                    }
                    if (args[0].equalsIgnoreCase("set")) {
                        if (args[1].equalsIgnoreCase("soul")) {
                            Data.setSoul(target, soul);
                            sendConsoleMessage(c, Objects.requireNonNull(getlanguagefile().getString("CHECK_SOUL")).replace("%player%", target.getName()).replace("%soul%", new DecimalFormat("###,###.###").format(soul)));
                        }
                        if (args[1].equalsIgnoreCase("max")) {
                            Data.setSoulMax(target, soul);
                            sendConsoleMessage(c, Objects.requireNonNull(getlanguagefile().getString("CHECK_MAX_SOUL")).replace("%player%", target.getName()).replace("%soul%", new DecimalFormat("###,###.###").format(soul)));
                        }
                    }
                } else {
                    sendConsoleMessage(c, "&cSomething is go wrong....");
                }
            }
        }
    }

    @Override
    public List<String> TabComplete(CommandSender sender, String[] args) {
        List<String> completions = new ArrayList<>();
        List<String> commands = new ArrayList<>();
        if (args.length == 1) {
            if (sender.hasPermission("soul.admin")) {
                commands.add("add");
                commands.add("remove");
                commands.add("set");
                commands.add("reload");
                commands.add("help");
            }
            if (sender.hasPermission("soul.user")) commands.add("check");
            if (sender.hasPermission("soul.transfer")) commands.add("transfer");
            StringUtil.copyPartialMatches(args[0], commands, completions);
        } else if (args.length == 2) {
            if (sender.hasPermission("soul.admin")) {
                if (args[0].equalsIgnoreCase("add") || args[0].equalsIgnoreCase("set") || args[0].equalsIgnoreCase("remove")) {
                    commands.add("soul");
                    commands.add("max");
                }
            }
            if (sender.hasPermission("soul.user") || sender.hasPermission("soul.transfer"))
                if (args[0].equalsIgnoreCase("check")
                        || args[0].equalsIgnoreCase("transfer")) {
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        commands.add(p.getName());
                    }
                }
            StringUtil.copyPartialMatches(args[1], commands, completions);
        } else if (args.length == 3) {
            if (sender.hasPermission("soul.admin")) {
                if (args[0].equalsIgnoreCase("add") || args[0].equalsIgnoreCase("set") || args[0].equalsIgnoreCase("remove")) {
                    if (args[1].equalsIgnoreCase("soul") || args[1].equalsIgnoreCase("max")) {
                        for (Player p : Bukkit.getOnlinePlayers()) {
                            commands.add(p.getName());
                        }
                    }
                }
            }
            if (sender.hasPermission("soul.transfer")) {
                if (args[0].equalsIgnoreCase("transfer") && sender instanceof Player p) {
                    for (int i = (Data.getSoul(p) - Data.getSoul(p) + 1); i <= Data.getSoul(p); i++) {
                        commands.add(String.valueOf(i));
                    }
                }
            }
            StringUtil.copyPartialMatches(args[2], commands, completions);
        } else if (args.length == 4) {
            if (sender.hasPermission("soul.admin")) {
                if (args[0].equalsIgnoreCase("add") || args[0].equalsIgnoreCase("set") || args[0].equalsIgnoreCase("remove")) {
                    if (args[1].equalsIgnoreCase("soul") || args[1].equalsIgnoreCase("max")) {
                        for (int i = 1; i <= 10; i++) {
                            commands.add(String.valueOf(i));
                        }
                    }
                }
            }
            StringUtil.copyPartialMatches(args[3], commands, completions);
        }
        Collections.sort(completions);
        return completions;
    }
}
