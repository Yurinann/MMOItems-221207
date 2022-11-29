package net.Indyuce.mmoitems.api;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class SoulboundInfo {

    /**
     * Used to store which items must be given back to which player
     */
    private static final Map<UUID, SoulboundInfo> INFO = new HashMap<>();
    /*
     * Items that need to be given to the player whenever he respawns.
     */
    private final List<ItemStack> items = new ArrayList<>();
    /*
     * If the player leaves the server before removing, the cached items will be
     * lost. the plugin saves the last location of the player to drop the items
     * when the server shuts down this way they are 'saved'
     */
    private final Location loc;
    private final Player player;

    /**
     * Instanced when a player dies if some souljbound items must be kept in the
     * player's inventory and need to be cached before the player respawns.
     * <p>
     * If the player leaves the server leaving one object of this type in server
     * RAM, the cached items need to be dropped if the server closes before the
     * player respawns again
     */
    public SoulboundInfo(Player player) {
        this.player = player;
        loc = player.getLocation().clone();
    }

    public static void read(Player player) {
        if (INFO.containsKey(player.getUniqueId())) {
            INFO.get(player.getUniqueId()).giveItems();
            INFO.remove(player.getUniqueId());
        }
    }

    /**
     * @return Soulbound info of players who have not clicked the respawn button
     * and yet have items cached in server RAM
     */
    public static Collection<SoulboundInfo> getAbandonnedInfo() {
        return INFO.values();
    }

    public void add(ItemStack item) {
        items.add(item);
    }

    public boolean hasItems() {
        return !items.isEmpty();
    }

    public void setup() {
        INFO.put(player.getUniqueId(), this);
    }

    public void giveItems() {
        for (ItemStack item : items)
            for (ItemStack drop : player.getInventory().addItem(item).values())
                player.getWorld().dropItem(player.getLocation(), drop);
    }

    public void dropItems() {
        items.forEach(item -> loc.getWorld().dropItem(loc, item));
    }
}
