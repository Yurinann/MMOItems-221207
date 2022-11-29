package net.Indyuce.mmoitems.stat;

import net.Indyuce.mmoitems.stat.type.BooleanStat;
import org.bukkit.Material;

public class LostWhenBroken extends BooleanStat {
    public LostWhenBroken() {
        super("WILL_BREAK", Material.SHEARS, "Lost when Broken?", new String[]{"If set to true, the item will be lost", "once it reaches 0 durability."}, new String[]{"!block", "all"});
    }
}
