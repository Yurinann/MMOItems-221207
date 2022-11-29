package net.Indyuce.mmoitems.api.event.item;

import net.Indyuce.mmoitems.api.event.PlayerDataEvent;
import net.Indyuce.mmoitems.api.interaction.weapon.untargeted.UntargetedWeapon;
import net.Indyuce.mmoitems.api.player.PlayerData;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class UntargetedWeaponUseEvent extends PlayerDataEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private final UntargetedWeapon weapon;
    private boolean cancelled;

    /**
     * Called when performing a special weapon attack using a staff or gauntlet
     *
     * @param who    Player attacking
     * @param weapon Weapon being used
     */
    public UntargetedWeaponUseEvent(@NotNull PlayerData who, @NotNull UntargetedWeapon weapon) {
        super(who);

        this.weapon = weapon;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    /**
     * @return The weapon used by the player that fired this event
     */
    @NotNull
    public UntargetedWeapon getWeapon() {
        return weapon;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        cancelled = cancel;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
}
