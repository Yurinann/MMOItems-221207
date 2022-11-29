package net.Indyuce.mmoitems.comp.mmocore;

import io.lumine.mythic.lib.version.VersionMaterial;
import net.Indyuce.mmocore.MMOCore;
import net.Indyuce.mmocore.api.event.AsyncPlayerDataLoadEvent;
import net.Indyuce.mmocore.api.event.PlayerChangeClassEvent;
import net.Indyuce.mmocore.api.event.PlayerLevelUpEvent;
import net.Indyuce.mmocore.api.event.PlayerResourceUpdateEvent;
import net.Indyuce.mmocore.api.player.attribute.PlayerAttribute;
import net.Indyuce.mmocore.api.player.stats.StatType;
import net.Indyuce.mmocore.experience.Profession;
import net.Indyuce.mmoitems.MMOItems;
import net.Indyuce.mmoitems.api.player.PlayerData;
import net.Indyuce.mmoitems.api.player.RPGPlayer;
import net.Indyuce.mmoitems.comp.mmocore.stat.ExtraAttribute;
import net.Indyuce.mmoitems.comp.mmocore.stat.RequiredAttribute;
import net.Indyuce.mmoitems.comp.mmocore.stat.RequiredProfession;
import net.Indyuce.mmoitems.comp.rpg.RPGHandler;
import net.Indyuce.mmoitems.stat.type.DoubleStat;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Locale;

public class MMOCoreHook implements RPGHandler, Listener {

    /**
     * Called when MMOItems enables
     * <p>
     * These stats are only updated on a server reload because that
     * class has to be instantiated again for the registered stats to update
     */
    public MMOCoreHook() {
        for (PlayerAttribute attribute : MMOCore.plugin.attributeManager.getAll()) {
            MMOItems.plugin.getStats().register(new RequiredAttribute(attribute));
            MMOItems.plugin.getStats().register(new ExtraAttribute(attribute));
        }

        for (Profession profession : MMOCore.plugin.professionManager.getAll()) {

            // Adds profession specific Additional Experience stats.
            MMOItems.plugin.getStats().register(new DoubleStat((StatType.ADDITIONAL_EXPERIENCE.name() + '_' + profession.getId())
                    .replace('-', '_').replace(' ', '_').toUpperCase(Locale.ROOT),
                    VersionMaterial.EXPERIENCE_BOTTLE.toMaterial(), profession.getName() + ' ' + "Additional Experience (MMOCore)"
                    , new String[]{"Additional MMOCore profession " + profession.getName() + " experience in %."}, new String[]{"!block", "all"}));
            MMOItems.plugin.getStats().register(new RequiredProfession(profession));
        }
    }

    @Override
    public void refreshStats(PlayerData data) {
    }

    @Override
    public RPGPlayer getInfo(PlayerData data) {
        return new MMOCoreRPGPlayer(data);
    }

    @EventHandler
    public void updateInventoryOnLevelUp(PlayerLevelUpEvent event) {
        PlayerData.get(event.getPlayer()).getInventory().scheduleUpdate();
    }

    @EventHandler
    public void updateInventoryOnClassChange(PlayerChangeClassEvent event) {
        PlayerData.get(event.getPlayer()).getInventory().scheduleUpdate();
    }

    /**
     * Updates inventory when player data has finished loading. This may
     * cause issues because in some cases the MMOCore player data is done
     * loading before MI data is even initialized in which case MI should
     * not do anymore.
     * <p>
     * Fixes https://gitlab.com/phoenix-dvpmt/mmocore/-/issues/545
     */
    @EventHandler
    public void updateInventoryOnLoad(AsyncPlayerDataLoadEvent event) {
        if (PlayerData.has(event.getPlayer()))
            PlayerData.get(event.getPlayer()).getInventory().scheduleUpdate();
    }

    public static class MMOCoreRPGPlayer extends RPGPlayer {
        private final net.Indyuce.mmocore.api.player.PlayerData data;

        public MMOCoreRPGPlayer(PlayerData playerData) {
            super(playerData);

            data = net.Indyuce.mmocore.api.player.PlayerData.get(playerData.getUniqueId());
        }

        public net.Indyuce.mmocore.api.player.PlayerData getData() {
            return data;
        }

        @Override
        public int getLevel() {
            return data.getLevel();
        }

        @Override
        public String getClassName() {
            return data.getProfess().getName();
        }

        @Override
        public double getMana() {
            return data.getMana();
        }

        @Override
        public void setMana(double value) {
            data.setMana(value);
        }

        @Override
        public double getStamina() {
            return data.getStamina();
        }

        @Override
        public void setStamina(double value) {
            data.setStamina(value);
        }

        @Override
        public void giveMana(double value) {
            data.giveMana(value, PlayerResourceUpdateEvent.UpdateReason.OTHER);
        }

        @Override
        public void giveStamina(double value) {
            data.giveStamina(value, PlayerResourceUpdateEvent.UpdateReason.OTHER);
        }
    }
}