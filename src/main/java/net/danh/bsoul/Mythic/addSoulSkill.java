package net.danh.bsoul.Mythic;

import io.lumine.mythic.api.adapters.AbstractEntity;
import io.lumine.mythic.api.config.MythicLineConfig;
import io.lumine.mythic.api.skills.ITargetedEntitySkill;
import io.lumine.mythic.api.skills.SkillMetadata;
import io.lumine.mythic.api.skills.SkillResult;
import io.lumine.mythic.bukkit.BukkitAdapter;
import net.danh.bsoul.Manager.Data;
import org.bukkit.entity.Player;

public class addSoulSkill implements ITargetedEntitySkill {

    protected final int amount;

    public addSoulSkill(MythicLineConfig config){
        amount = config.getInteger(new String[] {"amount", "a"},1);
    }
    @Override
    public SkillResult castAtEntity(SkillMetadata skillMetadata, AbstractEntity target) {
        if (target.isPlayer()) {
            Player p = (Player) BukkitAdapter.adapt(target);
            if (p != null) {
                Data.addSoul(p, amount);
            }
        }
        return SkillResult.SUCCESS;
    }
}
