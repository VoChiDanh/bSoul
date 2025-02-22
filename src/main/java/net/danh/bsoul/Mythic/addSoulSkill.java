package net.danh.bsoul.Mythic;

import io.lumine.mythic.api.adapters.AbstractEntity;
import io.lumine.mythic.api.config.MythicLineConfig;
import io.lumine.mythic.api.skills.ITargetedEntitySkill;
import io.lumine.mythic.api.skills.SkillMetadata;
import io.lumine.mythic.api.skills.SkillResult;
import io.lumine.mythic.bukkit.BukkitAdapter;
import net.danh.bsoul.Manager.Data;
import net.danh.bsoul.bSoul;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class addSoulSkill implements ITargetedEntitySkill {

    protected final int amount;

    public addSoulSkill(MythicLineConfig config) {
        amount = config.getInteger(new String[]{"amount", "a"}, 1);
    }

    @Override
    public SkillResult castAtEntity(SkillMetadata skillMetadata, AbstractEntity target) {
        if (target.isPlayer()) {
            Player p = (Player) BukkitAdapter.adapt(target);
            if (p != null) {
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        Data.addSoul(p, amount);
                    }
                }.runTask(bSoul.getInstance());
            }
        }
        return SkillResult.SUCCESS;
    }
}
