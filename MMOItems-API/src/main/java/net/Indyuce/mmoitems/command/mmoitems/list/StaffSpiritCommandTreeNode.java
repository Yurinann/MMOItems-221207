package net.Indyuce.mmoitems.command.mmoitems.list;

import io.lumine.mythic.lib.command.api.CommandTreeNode;
import net.Indyuce.mmoitems.stat.StaffSpiritStat.StaffSpirit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class StaffSpiritCommandTreeNode extends CommandTreeNode {
    public StaffSpiritCommandTreeNode(CommandTreeNode parent) {
        super(parent, "spirit");
    }

    @Override
    public CommandResult execute(CommandSender sender, String[] args) {
        sender.sendMessage(ChatColor.DARK_GRAY + "" + ChatColor.STRIKETHROUGH + "-----------------[" + ChatColor.LIGHT_PURPLE + " Staff Spirits "
                + ChatColor.DARK_GRAY + "" + ChatColor.STRIKETHROUGH + "]-----------------");
        for (StaffSpirit ss : StaffSpirit.values()) {
            String lore = !ss.hasLore() ? " " + ChatColor.WHITE + ">> " + ChatColor.GRAY + "" + ChatColor.ITALIC + ss.getLore() : "";
            sender.sendMessage("* " + ChatColor.LIGHT_PURPLE + ss.getName() + lore);
        }
        return CommandResult.SUCCESS;
    }
}
