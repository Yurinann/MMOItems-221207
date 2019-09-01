package net.Indyuce.mmoitems.version.durability.stat;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;

import net.Indyuce.mmoitems.api.item.MMOItem;
import net.Indyuce.mmoitems.api.item.NBTItem;
import net.Indyuce.mmoitems.api.item.build.MMOItemBuilder;
import net.Indyuce.mmoitems.stat.data.StatData;
import net.Indyuce.mmoitems.stat.type.DoubleStat;
import net.Indyuce.mmoitems.stat.type.ItemStat;

public class DefaultDurability extends DoubleStat {
	public DefaultDurability() {
		super(new ItemStack(Material.FISHING_ROD), "Default Durability/ID", new String[] { "The durability of your item." }, "durability", new String[] { "all" });
	}

	@Override
	public boolean whenApplied(MMOItemBuilder item, StatData data) {
		if (item.getMeta() instanceof Damageable)
			((Damageable) item.getMeta()).setDamage((int) ((DoubleData) data).generateNewValue());
		return true;
	}

	@Override
	public void whenLoaded(MMOItem mmoitem, NBTItem item) {
		if (item.getItem().getItemMeta() instanceof Damageable)
			mmoitem.setData(ItemStat.DURABILITY, new DoubleData(((Damageable) item.getItem().getItemMeta()).getDamage()));
	}
}
