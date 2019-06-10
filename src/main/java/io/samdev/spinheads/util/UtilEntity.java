package io.samdev.spinheads.util;

import org.bukkit.World;
import org.bukkit.entity.Entity;

import java.util.UUID;

public final class UtilEntity
{
    private UtilEntity() {}

    public static Entity getEntityFromId(UUID uuid, World world)
    {
        for (Entity entity : world.getEntities())
        {
            if (entity.getUniqueId().equals(uuid))
            {
                return entity;
            }
        }

        return null;
    }
}
