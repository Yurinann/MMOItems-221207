package net.Indyuce.mmoitems;

import net.Indyuce.mmoitems.api.player.PlayerData;
import net.Indyuce.mmoitems.comp.PhatLootsHook;
import net.Indyuce.mmoitems.gui.listener.GuiListener;
import net.Indyuce.mmoitems.listener.*;
import org.bukkit.Bukkit;

public class MMOItemsBukkit {

    /**
     * Called when MMOItems enables. This registers
     * all the listeners required for MMOItems to run
     */
    public MMOItemsBukkit(MMOItems plugin) {

        Bukkit.getPluginManager().registerEvents(new ItemUse(), plugin);
        Bukkit.getPluginManager().registerEvents(new ItemListener(), plugin);
        Bukkit.getPluginManager().registerEvents(new PlayerListener(), plugin);
        Bukkit.getPluginManager().registerEvents(new CustomSoundListener(), plugin);
        Bukkit.getPluginManager().registerEvents(new DurabilityListener(), plugin);
        Bukkit.getPluginManager().registerEvents(new DisableInteractions(), plugin);
        Bukkit.getPluginManager().registerEvents(new GuiListener(), plugin);
        Bukkit.getPluginManager().registerEvents(new CustomBlockListener(), plugin);
        if (Bukkit.getPluginManager().getPlugin("PhatLoots") != null)
            Bukkit.getPluginManager().registerEvents(new PhatLootsHook(), plugin);

        Bukkit.getScheduler().runTaskTimer(plugin, () -> Bukkit.getOnlinePlayers().forEach(player -> PlayerData.get(player).updateStats()), 100, 20);
    }
}