package net.Indyuce.mmoitems.gui.edition.recipe.rba;

import io.lumine.mythic.lib.api.util.ItemFactory;
import io.lumine.mythic.lib.api.util.ui.QuickNumberRange;
import io.lumine.mythic.lib.api.util.ui.SilentNumbers;
import net.Indyuce.mmoitems.gui.edition.recipe.gui.RecipeMakerGUI;
import net.Indyuce.mmoitems.gui.edition.recipe.rba.type.RBA_DoubleButton;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Experience from furnace recipes and stuff
 *
 * @author Gunging
 */
public class RBA_Experience extends RBA_DoubleButton {

    public static final String FURNACE_EXPERIENCE = "exp";
    public static final double DEFAULT = 0.35;
    @NotNull
    final ItemStack doubleButton = RecipeMakerGUI.addLore(ItemFactory.of(Material.EXPERIENCE_BOTTLE).name("\u00a7aExperience").lore(SilentNumbers.chop(
            "This recipe gives experience when crafted, how much?"
            , 65, "\u00a77")).build(), SilentNumbers.toArrayList(""));

    /**
     * A button of an Edition Inventory. Nice!
     *
     * @param inv The edition inventory this is a button of
     */
    public RBA_Experience(@NotNull RecipeMakerGUI inv) {
        super(inv);
    }

    @NotNull
    @Override
    public String getDoubleConfigPath() {
        return FURNACE_EXPERIENCE;
    }

    @Nullable
    @Override
    public QuickNumberRange getRange() {
        return new QuickNumberRange(0D, null);
    }

    @Override
    public boolean requireInteger() {
        return false;
    }

    @Override
    public double getDefaultValue() {
        return DEFAULT;
    }

    @NotNull
    @Override
    public ItemStack getDoubleButton() {
        return doubleButton;
    }
}
