package net.Indyuce.mmoitems.command.mmoitems.item;

import io.lumine.mythic.lib.command.api.CommandTreeNode;
import org.bukkit.command.CommandSender;

public class ItemCommandTreeNode extends CommandTreeNode {
    public ItemCommandTreeNode(CommandTreeNode parent) {
        super(parent, "item");

        addChild(new IdentifyCommandTreeNode(this));
        addChild(new UnidentifyCommandTreeNode(this));
        addChild(new RepairCommandTreeNode(this));
        addChild(new DeconstructCommandTreeNode(this));
    }

    @Override
    public CommandResult execute(CommandSender sender, String[] args) {
        return CommandResult.THROW_USAGE;
    }
}
