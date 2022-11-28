package net.Indyuce.mmoitems.util;

import net.Indyuce.mmoitems.MMOItems;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;
import java.util.logging.Level;

/**
 * mmoitems
 *
 * @author Roch Blondiaux
 * @date 24/10/2022
 */
public class PluginUtils {

    public PluginUtils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Checks if a plugin is enabled and calls a consumer if it is.
     *
     * @param name     The name of the plugin
     * @param callback The callback to execute if the plugin is enabled
     */
    public static void isDependencyPresent(@NotNull String name, @NotNull Consumer<Void> callback) {
        if (Bukkit.getPluginManager().getPlugin(name) != null)
            callback.accept(null);
    }

    /**
     * Checks if a plugin is enabled and logs a warning if it's not.
     *
     * @param name     The name of the plugin
     * @param callback The callback to execute if the plugin is enabled
     */

    public static void hookDependencyIfPresent(@NotNull String name, @NotNull Consumer<Void> callback) {
        if (Bukkit.getPluginManager().getPlugin(name) == null)
            return;
        callback.accept(null);
        MMOItems.plugin.getLogger().log(Level.INFO, "Hooked onto %s".formatted(name));
    }
}
