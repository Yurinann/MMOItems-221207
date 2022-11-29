package net.Indyuce.mmoitems.stat.data;

import net.Indyuce.mmoitems.api.item.build.MMOItemBuilder;
import net.Indyuce.mmoitems.stat.data.random.RandomStatData;
import net.Indyuce.mmoitems.stat.data.type.StatData;
import org.apache.commons.lang.Validate;
import org.bukkit.Material;

public class MaterialData implements StatData, RandomStatData<MaterialData> {
    private Material material;

    /*
     * material must not be null because it is called directly in the
     * MMOBuilder constructor.
     */
    public MaterialData(Material material) {
        Validate.notNull(material, "Material must not be null");
        this.material = material;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        Validate.notNull(material, "Material must not be null");
        this.material = material;
    }

    @Override
    public MaterialData randomize(MMOItemBuilder builder) {
        return this;
    }
}