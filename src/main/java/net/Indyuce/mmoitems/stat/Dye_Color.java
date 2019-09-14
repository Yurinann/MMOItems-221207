package net.Indyuce.mmoitems.stat;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import net.Indyuce.mmoitems.MMOItems;
import net.Indyuce.mmoitems.api.ConfigFile;
import net.Indyuce.mmoitems.api.edition.StatEdition;
import net.Indyuce.mmoitems.api.item.MMOItem;
import net.Indyuce.mmoitems.api.item.NBTItem;
import net.Indyuce.mmoitems.api.item.build.MMOItemBuilder;
import net.Indyuce.mmoitems.api.util.AltChar;
import net.Indyuce.mmoitems.gui.edition.EditionInventory;
import net.Indyuce.mmoitems.stat.data.ColorData;
import net.Indyuce.mmoitems.stat.data.StatData;
import net.Indyuce.mmoitems.stat.type.ItemStat;
import net.Indyuce.mmoitems.stat.type.StringStat;
import net.Indyuce.mmoitems.version.VersionMaterial;

public class Dye_Color extends StringStat {
	public Dye_Color() {
		super(VersionMaterial.RED_DYE.toItem(), "Dye Color", new String[] { "The color of your item", "(for leather armor sets).", "In RGB." }, "dye-color", new String[] { "all" }, Material.LEATHER_HELMET, Material.LEATHER_CHESTPLATE, Material.LEATHER_LEGGINGS, Material.LEATHER_BOOTS);
	}

	@Override
	public boolean whenClicked(EditionInventory inv, InventoryClickEvent event) {
		ConfigFile config = inv.getItemType().getConfigFile();
		if (event.getAction() == InventoryAction.PICKUP_ALL)
			new StatEdition(inv, ItemStat.DYE_COLOR).enable("Write in the chat the RGB color you want.", ChatColor.AQUA + "Format: [RED] [GREEN] [BLUE]");

		if (event.getAction() == InventoryAction.PICKUP_HALF) {
			config.getConfig().set(inv.getItemId() + ".dye-color", null);
			inv.registerItemEdition(config);
			inv.open();
			inv.getPlayer().sendMessage(MMOItems.plugin.getPrefix() + "Successfully removed Dye Color.");
		}
		return true;
	}

	@Override
	public boolean whenInput(EditionInventory inv, ConfigFile config, String message, Object... info) {
		String[] split = message.split("\\ ");
		if (split.length != 3) {
			inv.getPlayer().sendMessage(MMOItems.plugin.getPrefix() + ChatColor.RED + message + " is not a valid [RED] [GREEN] [BLUE].");
			inv.getPlayer().sendMessage(MMOItems.plugin.getPrefix() + ChatColor.RED + "Example: '75 0 130' stands for Indigo Purple.");
			return false;
		}
		for (String s : split)
			try {
				Integer.parseInt(s);
			} catch (Exception e1) {
				inv.getPlayer().sendMessage(MMOItems.plugin.getPrefix() + s + " is not a valid number.");
				return false;
			}

		config.getConfig().set(inv.getItemId() + ".dye-color", message);
		inv.registerItemEdition(config);
		inv.open();
		inv.getPlayer().sendMessage(MMOItems.plugin.getPrefix() + "Dye Color successfully changed to " + message + ".");
		return true;
	}

	@Override
	public void whenDisplayed(List<String> lore, FileConfiguration config, String path) {
		lore.add("");
		if (!config.getConfigurationSection(path).contains("dye-color")) {
			lore.add(ChatColor.GRAY + "Current Value:");
			lore.add(ChatColor.RED + "No value.");
		} else
			lore.add(ChatColor.GRAY + "Current Value: " + ChatColor.GREEN + config.getString(path + ".dye-color"));
		lore.add("");
		lore.add(ChatColor.YELLOW + AltChar.listDash + " Click to change this value.");
		lore.add(ChatColor.YELLOW + AltChar.listDash + " Right click to remove the dye color.");
	}

	@Override
	public boolean whenLoaded(MMOItem item, ConfigurationSection config) {
		item.setData(ItemStat.DYE_COLOR, new ColorData(item, config.getString("dye-color")));
		return true;
	}

	@Override
	public void whenLoaded(MMOItem mmoitem, NBTItem item) {
		if (item.getItem().getItemMeta() instanceof LeatherArmorMeta)
			mmoitem.setData(ItemStat.DYE_COLOR, new ColorData(((LeatherArmorMeta) item.getItem().getItemMeta()).getColor()));
	}

	@Override
	public boolean whenApplied(MMOItemBuilder item, StatData data) {
		if (item.getMeta() instanceof LeatherArmorMeta)
			((LeatherArmorMeta) item.getMeta()).setColor(((ColorData) data).getColor());
		return true;
	}
}