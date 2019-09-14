package net.Indyuce.mmoitems.stat;

import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import net.Indyuce.mmoitems.api.item.MMOItem;
import net.Indyuce.mmoitems.api.item.NBTItem;
import net.Indyuce.mmoitems.api.item.build.MMOItemBuilder;
import net.Indyuce.mmoitems.stat.data.StatData;
import net.Indyuce.mmoitems.stat.type.BooleanStat;
import net.Indyuce.mmoitems.version.nms.ItemTag;

public class Unbreakable extends BooleanStat {
	public Unbreakable() {
		super(new ItemStack(Material.ANVIL), "Unbreakable", new String[] { "Infinite durability if set to true." }, "unbreakable", new String[] { "all" });
	}

	@Override
	public boolean whenApplied(MMOItemBuilder item, StatData data) {
		if (((BooleanData) data).isEnabled()) {
			item.addItemTag(new ItemTag("Unbreakable", true));
			item.getMeta().addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
		}
		return true;
	}

	@Override
	public void whenLoaded(MMOItem mmoitem, NBTItem item) {
		if (item.hasTag("Unbreakable"))
			mmoitem.setData(this, new BooleanData(item.getBoolean("Unbreakable")));
	}
}