package net.Indyuce.mmoitems.comp.inventory;

import net.Indyuce.mmoitems.api.player.inventory.EquippedItem;
import org.bukkit.entity.Player;

import java.util.List;

public interface PlayerInventory {
    List<EquippedItem> getInventory(Player player);
}
