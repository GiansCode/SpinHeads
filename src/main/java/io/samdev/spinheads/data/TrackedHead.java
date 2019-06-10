package io.samdev.spinheads.data;

import io.samdev.spinheads.head.HeadData;
import io.samdev.spinheads.util.UtilEntity;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class TrackedHead
{
    private final UUID uuid;

    private final HeadData headData;

    private final Location location;

    private final UUID headEntity;

    private final List<UUID> hologramEntities;

    public TrackedHead(UUID uuid, HeadData headData, Location location, UUID headEntity, List<UUID> hologramEntities)
    {
        this.uuid = uuid;
        this.headData = headData;
        this.location = location;
        this.headEntity = headEntity;
        this.hologramEntities = hologramEntities;
    }

    void despawn()
    {
        location.getBlock().setType(Material.AIR);

        getCachedEntity().remove();
        hologramEntities
            .stream()
            .map(uuid -> UtilEntity.getEntityFromId(uuid, location.getWorld()))
            .filter(Objects::nonNull)
            .forEach(Entity::remove);
    }

    private Entity cachedEntity;

    public void setCachedEntity(Entity cachedEntity)
    {
        this.cachedEntity = cachedEntity;
    }

    public Entity getCachedEntity()
    {
        return cachedEntity;
    }

    public UUID getUuid()
    {
        return uuid;
    }

    public HeadData getHeadData()
    {
        return headData;
    }

    public Location getLocation()
    {
        return location;
    }

    public UUID getHeadEntity()
    {
        return headEntity;
    }

    public List<UUID> getHologramEntities()
    {
        return hologramEntities;
    }
}
