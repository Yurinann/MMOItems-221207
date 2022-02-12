package net.Indyuce.mmoitems.comp.mythicmobs.mechanics;

import io.lumine.mythic.lib.api.crafting.uimanager.ProvidedUIFilter;
import io.lumine.mythic.lib.api.crafting.uimanager.UIFilterManager;
import io.lumine.mythic.lib.api.util.ui.FriendlyFeedbackCategory;
import io.lumine.mythic.lib.api.util.ui.FriendlyFeedbackMessage;
import io.lumine.mythic.lib.api.util.ui.FriendlyFeedbackProvider;
import io.lumine.mythic.lib.api.util.ui.SilentNumbers;
import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.adapters.AbstractLocation;
import io.lumine.xikage.mythicmobs.adapters.SkillAdapter;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.*;
import io.lumine.xikage.mythicmobs.skills.placeholders.parsers.PlaceholderDouble;
import io.lumine.xikage.mythicmobs.skills.placeholders.parsers.PlaceholderFloat;
import io.lumine.xikage.mythicmobs.skills.placeholders.parsers.PlaceholderInt;
import net.Indyuce.mmoitems.MMOItems;
import net.Indyuce.mmoitems.api.util.message.FFPMMOItems;
import net.Indyuce.mmoitems.listener.ItemUse;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

/**
 * An arrow volley method but uses the stats of the bow
 * held by the player that is casting this skill, or at
 * least a scaled version of them!
 *
 * @author Gunging
 */
public class MMOItemsArrowVolleyMechanic extends SkillMechanic implements ITargetedEntitySkill, ITargetedLocationSkill {

    @NotNull PlaceholderInt amount, spread, fireTicks, removeDelay;
    @NotNull PlaceholderFloat velocity, scale;
    @NotNull PlaceholderDouble xOffset, yOffset, zOffset, fOffset, sOffset;

    @Nullable ItemStack arrowItem = null;
    boolean fullEvent;
    boolean scalePerArrow;
    boolean fromOrigin;

    public MMOItemsArrowVolleyMechanic(String line, MythicLineConfig mlc) {
        super(line, mlc);
        threadSafetyLevel = ThreadSafetyLevel.SYNC_ONLY;

        amount = mlc.getPlaceholderInteger(new String[] {"amount", "arrows", "a"}, 20);
        spread = mlc.getPlaceholderInteger(new String[] {"spread", "s"}, 45);
        fireTicks = mlc.getPlaceholderInteger(new String[] {"fireticks", "ft", "f"}, 0);
        removeDelay = mlc.getPlaceholderInteger(new String[] {"removedelay", "rd", "r"}, 200);
        velocity = mlc.getPlaceholderFloat(new String[] {"velocity", "v"}, 20);
        scale = mlc.getPlaceholderFloat(new String[] {"statsscale", "ss"}, 1);

        fullEvent = mlc.getBoolean(new String[] {"fullevent", "fe"}, false);
        scalePerArrow = mlc.getBoolean(new String[] {"scaleperarrow", "spa"}, false);
        fromOrigin = mlc.getBoolean(new String[] {"fromorigin", "fo"}, false);

        //region Get Arrow Item
        String itemFilter = mlc.getString(new String[] {"arrowitem", "item", "ai"}, null);
        //DBG//MMOItems.log("Found In Config " + itemFilter);
        if (itemFilter != null) {
            //DBG//FriendlyFeedbackProvider ffp = new FriendlyFeedbackProvider(FFPMMOItems.get());
            ProvidedUIFilter uiFilter = UIFilterManager.getUIFilter(itemFilter.replace("<&sp>", " ").replace("\"", ""), null);
            //DBG//MMOItems.log("Errors Result: " + SilentNumbers.collapseList(SilentNumbers.transcribeList(ffp.getFeedbackOf(FriendlyFeedbackCategory.ERROR), message -> { if (message instanceof FriendlyFeedbackMessage) { return ((FriendlyFeedbackMessage) message).forConsole(FFPMMOItems.get()); } return ""; }), ""));
            if (uiFilter != null) {
                if (uiFilter.isValid(null) && uiFilter.getParent().fullyDefinesItem()) {
                    uiFilter.setAmount(1);

                    // Generate Item
                    arrowItem = uiFilter.getItemStack(null);
                }
                //DBG// else{ MMOItems.log("Invalid\u00a73 " + uiFilter.toString()); }
            }
        }

        //DBG//MMOItems.log("Generated Item " + SilentNumbers.getItemName(arrowItem));
        //endregion

        // Offsets
        xOffset = mlc.getPlaceholderDouble(new String[] {"startxoffset", "sxo"}, 0);
        yOffset = mlc.getPlaceholderDouble(new String[] {"startyoffset", "syo"}, 3);
        zOffset = mlc.getPlaceholderDouble(new String[] {"startzoffset", "szo"}, 0);
        fOffset = mlc.getPlaceholderDouble(new String[] {"startfoffset", "sfo"}, 0);
        sOffset = mlc.getPlaceholderDouble(new String[] {"startsoffset", "sso"}, 0);
    }


    @Override
    public boolean castAtLocation(SkillMetadata data, AbstractLocation target) {

        // Caster must be a player
        if (data.getCaster().getEntity().getBukkitEntity() instanceof Player) {

            // MMOItems Volley!
            executeMIVolley(data.getCaster(), data, target, amount.get(data), velocity.get(data) * 0.1F, spread.get(data), fireTicks.get(data), removeDelay.get(data), scale);

        } else {

            // Run as normal mythicmobs arrow volley
            SkillAdapter.get().executeVolley(data.getCaster(), target, amount.get(data), velocity.get(data) * 0.1F, spread.get(data), fireTicks.get(data), removeDelay.get(data));
        }
        return true;
    }

    @Override
    public boolean castAtEntity(SkillMetadata data, AbstractEntity target) {

        // Caster must be a player
        if (data.getCaster().getEntity().getBukkitEntity() instanceof Player) {

            // MMOItems Volley!
            executeMIVolley(data.getCaster(), data, target.getLocation(), amount.get(data,target), velocity.get(data) * 0.1F, spread.get(data), fireTicks.get(data), removeDelay.get(data), scale);

        } else {

            // Run as normal mythicmobs arrow volley
            SkillAdapter.get().executeVolley(data.getCaster(), target.getLocation(), amount.get(data,target), velocity.get(data) * 0.1F, spread.get(data), fireTicks.get(data), removeDelay.get(data));
        }
        return true;
    }


    public void executeMIVolley(@NotNull SkillCaster caster, @NotNull SkillMetadata data, @NotNull AbstractLocation t, int amount, float velocity, float spread, int fireTicks, int removeDelay, @NotNull PlaceholderFloat statsMultiplier)  {

        // Cancel infinite loops
        if (syncEventBlock) { return; }

        // Skill caster MUST be a  player
        if (!(caster.getEntity().getBukkitEntity() instanceof Player)) { return; }
        Player player = (Player) caster.getEntity().getBukkitEntity();

        // Target yeah
        Location target = BukkitAdapter.adapt(t);
        Location spawn = BukkitAdapter.adapt(fromOrigin ? data.getOrigin() : caster.getLocation()).clone();

        //region Calculate Offsets
        Vector forward = spawn.getDirection().normalize();
        Vector side = (new Vector(-forward.getZ(), 0.00001, forward.getX())).normalize();

        double fS = fOffset.get(data);
        double sS = sOffset.get(data);

        spawn.setX(spawn.getX() + xOffset.get(data) + (forward.getX() * fS) + (side.getX() * sS));
        spawn.setY(spawn.getY() + yOffset.get(data) + (forward.getY() * fS) + (side.getY() * sS));
        spawn.setZ(spawn.getZ() + zOffset.get(data) + (forward.getZ() * fS) + (side.getZ() * sS));
        //endregion

        // Direction vector
        Vector v = target.toVector().subtract(spawn.toVector()).normalize();

        // Player bow item is held??
        ItemStack bowItem = player.getInventory().getItemInMainHand().clone();
        ItemStack localArrowItem = (arrowItem != null ? arrowItem.clone() : new ItemStack(Material.ARROW));
        //DBG//MMOItems.log("Deployed Item " + SilentNumbers.getItemName(arrowItem));
        ItemUse use = new ItemUse();

        // Parse
        float arrowForce = statsMultiplier.get(data);

        // Spawn arrows
        ArrayList<Arrow> arrowList = new ArrayList<>();
        for (int i = 0; i < amount; i++) {

            // Spawn Arrow
            Arrow a = player.getWorld().spawnArrow(spawn, v, velocity, (spread/10.0F));
            a.setVelocity(a.getVelocity());

            // Identify arrow as the player's
            a.setShooter(player);

            // Run Event
            syncEventBlock = true;
            EntityShootBowEvent shootBowEvent = new EntityShootBowEvent(player, bowItem, localArrowItem, a, EquipmentSlot.HAND, arrowForce, false);
            if (fullEvent) { Bukkit.getPluginManager().callEvent(shootBowEvent); } else { use.handleCustomBows(shootBowEvent); }
            syncEventBlock = false;

            // Cancelled???
            if (shootBowEvent.isCancelled()) { a.remove(); continue; }

            // Set on fire I guess
            if(fireTicks > 0) { a.setFireTicks(fireTicks); }

            // Add to list
            arrowList.add(a);

            // Recalculate
            if (scalePerArrow) { arrowForce = statsMultiplier.get(data); }
        }

        // Remove after delay
        Bukkit.getScheduler().scheduleSyncDelayedTask(MMOItems.plugin, () -> {
            for (Arrow a : arrowList) { a.remove(); }arrowList.clear(); }, removeDelay);
    }

    static boolean syncEventBlock = false;
}