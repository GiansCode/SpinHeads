package io.samdev.spinheads.data;

import io.samdev.spinheads.SpinHeads;
import io.samdev.spinheads.head.HeadData;
import io.samdev.spinheads.util.UtilLocation;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import static java.util.stream.Collectors.toList;

public class DataManager
{
    private final SpinHeads plugin;

    public DataManager(SpinHeads plugin)
    {
        this.plugin = plugin;

        loadDataFile();
    }

    private final Set<TrackedHead> trackedHeads = new HashSet<>();
    
    public void addHead(TrackedHead head)
    {
        trackedHeads.add(head);

        String path = "heads." + head.getUuid().toString() + ".";

        config.set(path + "head", head.getHeadData().getName());
        config.set(path + "location", head.getLocation().serialize());
        config.set(path + "headEntity", head.getHeadEntity().toString());
        config.set(path + "hologramEntities", head.getHologramEntities()
            .stream()
            .map(UUID::toString)
            .collect(toList())
        );
        
        save();
    }

    public Set<TrackedHead> getTrackedHeads()
    {
        return Collections.unmodifiableSet(trackedHeads);
    }

    public TrackedHead getTrackedHead(Location location)
    {
        for (TrackedHead trackedHead : trackedHeads)
        {
            if (UtilLocation.roughEquals(location, trackedHead.getLocation()))
            {
                return trackedHead;
            }
        }

        return null;
    }

    public boolean deleteEntity(Entity entity)
    {
        TrackedHead trackedHead = getTrackedHead(entity);

        if (trackedHead != null)
        {
            deleteHead(trackedHead);
            return true;
        }

        return false;
    }

    private void deleteHead(TrackedHead head)
    {
        head.despawn();
        trackedHeads.remove(head);

        config.set("heads." + head.getUuid().toString(), null);
    }

    public List<TrackedHead> getEntities(Player player, int radius)
    {
        return player.getNearbyEntities(radius, radius, radius)
            .stream()
            .map(this::getTrackedHead)
            .filter(Objects::nonNull)
            .collect(toList());
    }

    private TrackedHead getTrackedHead(Entity entity)
    {
        for (TrackedHead trackedHead : trackedHeads)
        {
            if (trackedHead.getHeadEntity().equals(entity.getUniqueId()))
            {
                return trackedHead;
            }
        }

        return null;
    }

    private File file;
    private YamlConfiguration config;

    public void loadDataFile()
    {
        file = new File(plugin.getDataFolder(), "data.yml");

        if (!file.exists())
        {
            try
            {
                file.createNewFile();
            }
            catch (IOException ex)
            {
                plugin.getLogger().severe("Cannot create data file");
                ex.printStackTrace();

                return;
            }
        }

        config = YamlConfiguration.loadConfiguration(file);
        loadTrackedHeads();
    }

    @SuppressWarnings("unchecked")
    private void loadTrackedHeads()
    {
        trackedHeads.clear();
        
        ConfigurationSection headsSection = config.getConfigurationSection("heads");

        if (headsSection == null)
        {
            return;
        }

        for (String key : headsSection.getKeys(false))
        {
            ConfigurationSection section = headsSection.getConfigurationSection(key);

            UUID uuid = UUID.fromString(key);

            HeadData headData = plugin.getHeadManager().getHead(section.getString("head"));
            Location location = Location.deserialize(((MemorySection) section.get("location")).getValues(false));

            UUID headEntity = UUID.fromString(section.getString("headEntity"));
            List<UUID> hologramEntities = section.getStringList("hologramEntities")
                .stream()
                .map(UUID::fromString)
                .collect(toList());

            if (headData == null)
            {
                plugin.getLogger().severe("Could not load head: Invalid head type: " + section.getString("head"));
                continue;
            }

            trackedHeads.add(new TrackedHead(uuid, headData, location, headEntity, hologramEntities));
        }
    }

    public void save()
    {
        try
        {
            config.save(file);
        }
        catch (IOException ex)
        {
            plugin.getLogger().severe("Unable to save data file");
            ex.printStackTrace();
        }
    }
}
