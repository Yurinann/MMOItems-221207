package net.Indyuce.mmoitems.listener;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockIgniteEvent.IgniteCause;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import net.Indyuce.mmoitems.MMOItems;
import net.Indyuce.mmoitems.api.CustomBlock;
import net.Indyuce.mmoitems.api.item.NBTItem;

public class CustomBlockListener implements Listener {
	Random rnd = new Random();

	public CustomBlockListener() {
		if (MMOItems.plugin.getLanguage().replaceMushroomDrops)
			Bukkit.getPluginManager().registerEvents(new MushroomReplacer(), MMOItems.plugin);
	}

	@EventHandler
	public void a(BlockPhysicsEvent event) {
		if (MMOItems.plugin.getCustomBlocks().isMushroomBlock(event.getChangedType())) {
			event.setCancelled(true);
			event.getBlock().getState().update(true, false);
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void b(BlockBreakEvent event) {
		Material type = event.getBlock().getType();

		if (MMOItems.plugin.getCustomBlocks().isMushroomBlock(type)) {
			CustomBlock block = CustomBlock.getFromData(event.getBlock().getBlockData());
			if (block != null) {
				event.setDropItems(false);
				event.setExpToDrop(event.getPlayer().getGameMode() == GameMode.CREATIVE ? 0 : CustomBlockListener.getPickaxePower(event.getPlayer()) >= block.getRequiredPower() ? block.getMaxXPDrop() == 0 && block.getMinXPDrop() == 0 ? 0 : rnd.nextInt((block.getMaxXPDrop() - block.getMinXPDrop()) + 1) + block.getMinXPDrop() : 0);
			}
		}
	}

	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void c(PlayerInteractEvent event) {
		if (!event.hasItem() || event.getAction() != Action.RIGHT_CLICK_BLOCK || event.getHand() != EquipmentSlot.HAND || event.getClickedBlock().getType().isInteractable())
			return;
		if (event.getItem().getType() == Material.CLAY_BALL) {
			NBTItem nbtItem = MMOItems.plugin.getNMS().getNBTItem(event.getItem());
			if (nbtItem.getInteger("MMOITEMS_BLOCK_ID") > 160 || nbtItem.getInteger("MMOITEMS_BLOCK_ID") < 1)
				return;

			CustomBlock block = MMOItems.plugin.getCustomBlocks().getBlock(nbtItem.getInteger("MMOITEMS_BLOCK_ID"));

			List<Material> mat = Arrays.asList(Material.GRASS, Material.TALL_GRASS, Material.SEAGRASS, Material.TALL_SEAGRASS, Material.FERN, Material.LARGE_FERN, Material.DEAD_BUSH, Material.SNOW);

			Block modify = mat.contains(event.getClickedBlock().getType()) ? event.getClickedBlock() : event.getClickedBlock().getRelative(event.getBlockFace());

			if (isStandingInside(event.getPlayer().getLocation(), modify.getLocation()))
				return;

			if (!mat.contains(modify.getType()))
				switch (modify.getType()) {
				case AIR:
				case CAVE_AIR:
				case STRUCTURE_VOID:
					break;
				default:
					return;
				}

			Block oldState = modify;
			Material cachedType = modify.getType();
			BlockData cachedData = modify.getBlockData();
			modify.setType(block.getType(), false);
			modify.setBlockData(block.getBlockData(), false);

			MMOItems.plugin.getNMS().playArmAnimation(event.getPlayer());
			modify.getWorld().playSound(event.getPlayer().getLocation(), MMOItems.plugin.getNMS().getBlockPlaceSound(modify), 0.8f, 1.0f);

			BlockPlaceEvent bpe = new BlockPlaceEvent(modify, oldState.getState(), event.getClickedBlock(), event.getItem(), event.getPlayer(), true, EquipmentSlot.HAND);
			Bukkit.getServer().getPluginManager().callEvent(bpe);
			if (bpe.isCancelled()) {
				modify.setType(cachedType);
				modify.setBlockData(cachedData);
			} else if (event.getPlayer().getGameMode() != GameMode.CREATIVE) {
				ItemStack stack = event.getItem();
				stack.setAmount(stack.getAmount() - 1);
				event.getPlayer().getInventory().setItemInMainHand(stack.getAmount() > 0 ? stack : null);
			}
		}
		/**
		 * else
		 * if(MMOItems.plugin.getCustomBlocks().isMushroomBlock(event.getItem().getType()))
		 * { event.setCancelled(true); Block modify =
		 * event.getClickedBlock().getRelative(event.getBlockFace());
		 * 
		 * if(isStandingInside(event.getPlayer().getLocation(),
		 * modify.getLocation())) return; if(modify.getType() != Material.AIR)
		 * return;
		 * 
		 * Block oldState = modify; modify.setType(event.getItem().getType(),
		 * false);
		 * modify.setBlockData(event.getItem().getType().createBlockData(),
		 * false);
		 * 
		 * BlockPlaceEvent bpe = new BlockPlaceEvent(modify,
		 * oldState.getState(), event.getClickedBlock(), event.getItem(),
		 * event.getPlayer(), true, EquipmentSlot.HAND);
		 * Bukkit.getServer().getPluginManager().callEvent(bpe);
		 * if(bpe.isCancelled()) modify.setType(Material.AIR); }
		 */
	}

	@EventHandler
	public void d(BlockIgniteEvent event) {
		if (event.isCancelled())
			return;
		if (event.getCause() == IgniteCause.LAVA || event.getCause() == IgniteCause.SPREAD) {
			BlockFace[] faces = { BlockFace.UP, BlockFace.DOWN, BlockFace.NORTH, BlockFace.SOUTH, BlockFace.WEST, BlockFace.EAST };
			for (BlockFace face : faces)
				if (MMOItems.plugin.getCustomBlocks().isMushroomBlock(event.getBlock().getRelative(face).getType()) && CustomBlock.getFromData(event.getBlock().getRelative(face).getBlockData()) != null)
					event.setCancelled(true);
		}
	}

	private boolean isStandingInside(Location p, Location b) {
		return (p.getBlockX() == b.getBlockX() && (p.getBlockY() == b.getBlockY() || p.getBlockY() + 1 == b.getBlockY()) && p.getBlockZ() == b.getBlockZ());
	}

	public static int getPickaxePower(Player player) {
		ItemStack item = player.getInventory().getItemInMainHand();
		if (item != null && item.getType() != Material.AIR) {
			NBTItem nbt = NBTItem.get(item);
			if (nbt.hasType()) 
				return nbt.getInteger("MMOITEMS_PICKAXE_POWER");
		}

		return 0;
	}

	public class MushroomReplacer implements Listener {
		@EventHandler
		public void d(BlockBreakEvent event) {
			if (event.isCancelled())
				return;
			if (MMOItems.plugin.getCustomBlocks().isMushroomBlock(event.getBlock().getType()) && MMOItems.plugin.getDropTables().hasSilkTouchTool(event.getPlayer()))
				event.setDropItems(false);
		}
	}
}