package net.Indyuce.mmoitems.api.event.item;

import io.lumine.mythic.lib.api.item.NBTItem;
import net.Indyuce.mmoitems.api.event.PlayerDataEvent;
import net.Indyuce.mmoitems.api.item.mmoitem.VolatileMMOItem;
import net.Indyuce.mmoitems.api.player.PlayerData;
import org.bukkit.event.HandlerList;

public class ApplySoulboundEvent extends PlayerDataEvent {
    private static final HandlerList handlers = new HandlerList();

    private final VolatileMMOItem consumable;
    private final NBTItem target;

    /**
     * Called when a player tries to apply soulbound onto an item
     *
     * @param playerData Player soulbinding the item
     * @param consumable Consumable used to bind the item
     * @param target     Item being soulbound
     */
    public ApplySoulboundEvent(PlayerData playerData, VolatileMMOItem consumable, NBTItem target) {
        super(playerData);

        this.consumable = consumable;
        this.target = target;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public VolatileMMOItem getConsumable() {
        return consumable;
    }

    public NBTItem getTargetItem() {
        return target;
    }

    public HandlerList getHandlers() {
        return handlers;
    }
}
