package net.danh.bsoul.Manager;

import net.danh.bsoul.NMS.NMSAssistant;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static net.danh.bsoul.Manager.Items.makeItem;
import static net.danh.bsoul.Manager.Resources.getconfigfile;

public class Item {

    public static ItemStack getSoulItems(Integer amount) {
        Material material = Material.getMaterial(Objects.requireNonNull(getconfigfile().getString("ITEM.SOUL.MATERIAL")));
        Short data = Short.parseShort(Objects.requireNonNull(getconfigfile().getString("ITEM.SOUL.DATA")));
        Boolean glow = getconfigfile().getBoolean("ITEM.SOUL.GLOW");
        Boolean unbreak = getconfigfile().getBoolean("ITEM.SOUL.UNBREAK");
        Boolean hide_flag = getconfigfile().getBoolean("ITEM.SOUL.HIDE_FLAG");
        List<String> lore = Items.Lore(getconfigfile().getStringList("ITEM.SOUL.LORE").stream().map(a -> a.replace("%soul%", String.format("%,d", amount))).collect(Collectors.toList()));
        String name = Objects.requireNonNull(Chat.colorize(Objects.requireNonNull(getconfigfile().getString("ITEM.SOUL.NAME"))).replace("%soul%", String.format("%,d", amount)));
        NMSAssistant nms = new NMSAssistant();
        if (material != null) {
            if (nms.isVersionLessThanOrEqualTo(13)) {
                return makeItem(material, data, 1, glow, unbreak, hide_flag, name, lore);
            }
            if (nms.isVersionGreaterThanOrEqualTo(14)) {
                ItemStack soul = makeItem(material, Short.parseShort(String.valueOf(0)), 1, glow, unbreak, hide_flag, name, lore);
                ItemMeta meta = soul.getItemMeta();
                Objects.requireNonNull(meta).setCustomModelData(Integer.parseInt(data.toString()));
                soul.setItemMeta(meta);
                return soul;
            }
        }
        return null;
    }
}
