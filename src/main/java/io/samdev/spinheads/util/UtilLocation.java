package io.samdev.spinheads.util;

import org.bukkit.Location;

public final class UtilLocation
{
    private UtilLocation() {}

    public static Location fixLocation(Location location)
    {
        Location newLoc = location.clone();

        newLoc.setX(location.getBlockX() + 0.5D);
        newLoc.setZ(location.getBlockZ() + 0.5D);
        newLoc.setY(location.getBlockY());

        newLoc.setYaw(0.0F);
        newLoc.setPitch(0.0F);

        return newLoc;
    }

    public static boolean roughEquals(Location loc1, Location loc2)
    {
        return
            loc1.getBlockX() == loc2.getBlockX() &&
            loc1.getBlockY() == loc2.getBlockY() &&
            loc1.getBlockZ() == loc2.getBlockZ();
    }
}
