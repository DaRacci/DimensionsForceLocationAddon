package dev.racci.dimensions.addons;

import me.xxastaspastaxx.dimensions.Dimensions;
import me.xxastaspastaxx.dimensions.addons.DimensionsAddon;
import me.xxastaspastaxx.dimensions.addons.DimensionsAddonPriority;
import me.xxastaspastaxx.dimensions.completePortal.CompletePortal;
import me.xxastaspastaxx.dimensions.customportal.CustomPortal;
import me.xxastaspastaxx.dimensions.events.CustomPortalIgniteEvent;
import me.xxastaspastaxx.dimensions.events.CustomPortalUseEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class ForceLocation extends DimensionsAddon implements Listener {

    static final String NODE = "Addon.ForceLocation";

    public ForceLocation() {
        super("ForceLocationAddon", "1.0.0", "Force a location for portals to link too.", DimensionsAddonPriority.HIGHEST);
    }

    @Override
    public void onEnable(final Dimensions plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(
        ignoreCancelled = true,
        priority = EventPriority.HIGHEST
    )
    public void onPortalIgnite(CustomPortalIgniteEvent e) {
        final var targetPortal = this.getTarget(e.getCompletePortal());
        if (targetPortal == null) return;

        e.getCompletePortal().setLinkedPortal(targetPortal);
    }

    @EventHandler(
        ignoreCancelled = true,
        priority = EventPriority.HIGHEST
    )
    public void onPortalUse(CustomPortalUseEvent e) {
        final var targetPortal = this.getTarget(e.getCompletePortal());
        if (targetPortal == null) return;
        if (targetPortal == e.getCompletePortal()) {
            e.setCancelled(true);
            return;
        }

        e.setDestinationPortal(targetPortal);
    }

    private CompletePortal getTarget(CompletePortal complete) {
        if (getOption(complete, "hasTarget") == null) return null;

        final var x = (double) getOption(complete, "targetPortalX");
        final var y = (double)getOption(complete, "targetPortalY");
        final var z = (double) getOption(complete, "targetPortalZ");
        final var targetLocation = new Location(complete.getCustomPortal().getWorld(), x, y, z, 0, 0);

        return Dimensions.getCompletePortalManager().getCompletePortal(targetLocation, false, false);
    }

    @Override
    public void registerPortal(YamlConfiguration portalConfig, CustomPortal portal) {
        if (!portalConfig.contains(NODE)) return;

        setOption(portal, "targetPortalX", portalConfig.getDouble(NODE + '.' + "targetPortalX"));
        setOption(portal, "targetPortalY", portalConfig.getDouble(NODE + '.' + "targetPortalY"));
        setOption(portal, "targetPortalZ", portalConfig.getDouble(NODE + '.' + "targetPortalZ"));

        setOption(portal, "hasTarget", true);
    }
}
