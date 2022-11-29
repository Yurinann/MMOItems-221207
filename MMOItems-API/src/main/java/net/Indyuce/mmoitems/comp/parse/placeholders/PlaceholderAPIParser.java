package net.Indyuce.mmoitems.comp.parse.placeholders;

import io.lumine.mythic.lib.MythicLib;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.OfflinePlayer;

@Deprecated
public class PlaceholderAPIParser implements PlaceholderParser {
    public PlaceholderAPIParser() {
        new MMOItemsPlaceholders().register();
    }

    @Override
    public String parse(OfflinePlayer player, String string) {
        return MythicLib.plugin.parseColors(PlaceholderAPI.setPlaceholders(player, string));
    }
}
