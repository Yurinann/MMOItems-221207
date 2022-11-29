package net.Indyuce.mmoitems.api.interaction.weapon.untargeted.lute;

import com.google.gson.JsonObject;
import io.lumine.mythic.lib.MythicLib;
import io.lumine.mythic.lib.UtilityMethods;
import io.lumine.mythic.lib.api.item.NBTItem;
import io.lumine.mythic.lib.comp.target.InteractionType;
import io.lumine.mythic.lib.damage.DamageType;
import io.lumine.mythic.lib.player.PlayerMetadata;
import net.Indyuce.mmoitems.MMOItems;
import net.Indyuce.mmoitems.api.util.SoundReader;
import net.Indyuce.mmoitems.stat.data.ProjectileParticlesData;
import net.Indyuce.mmoitems.util.MMOUtils;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.List;

public class BruteLuteAttack implements LuteAttackHandler {

    @Override
    public void handle(PlayerMetadata caster, double damage, NBTItem nbt, double range, Vector weight, SoundReader sound) {
        new BukkitRunnable() {
            final Vector vec = caster.getPlayer().getEyeLocation().getDirection().multiply(.4);
            final Location loc = caster.getPlayer().getEyeLocation();
            int ti = 0;

            public void run() {
                if (ti++ > range)
                    cancel();

                List<Entity> entities = MMOUtils.getNearbyChunkEntities(loc);
                for (int j = 0; j < 3; j++) {
                    loc.add(vec.add(weight));
                    if (loc.getBlock().getType().isSolid()) {
                        cancel();
                        break;
                    }

                    if (nbt.hasTag("MMOITEMS_PROJECTILE_PARTICLES")) {
                        JsonObject obj = MythicLib.plugin.getJson().parse(nbt.getString("MMOITEMS_PROJECTILE_PARTICLES"), JsonObject.class);
                        Particle particle = Particle.valueOf(obj.get("Particle").getAsString());
                        // If the selected particle is colored, use the provided color
                        if (ProjectileParticlesData.isColorable(particle)) {
                            double red = Double.parseDouble(String.valueOf(obj.get("Red")));
                            double green = Double.parseDouble(String.valueOf(obj.get("Green")));
                            double blue = Double.parseDouble(String.valueOf(obj.get("Blue")));
                            ProjectileParticlesData.shootParticle(caster.getPlayer(), particle, loc, red, green, blue);
                            // If it's not colored, just shoot the particle
                        } else {
                            ProjectileParticlesData.shootParticle(caster.getPlayer(), particle, loc, 0, 0, 0);
                        }
                        // If no particle has been provided via projectile particle attribute, default to this particle
                    } else
                        loc.getWorld().spawnParticle(Particle.NOTE, loc, 2, .1, .1, .1, 0);

                    if (j == 0) sound.play(loc, 2, (float) (.5 + (double) ti / range));

                    for (Entity target : entities)
                        if (UtilityMethods.canTarget(caster.getPlayer(), loc, target, InteractionType.OFFENSE_ACTION)) {
                            caster.attack((LivingEntity) target, damage, DamageType.WEAPON, DamageType.MAGIC, DamageType.PROJECTILE);
                            cancel();
                            return;
                        }
                }
            }
        }.runTaskTimer(MMOItems.plugin, 0, 1);
    }
}

