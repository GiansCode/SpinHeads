package io.samdev.spinheads.head;

import io.samdev.spinheads.SpinHeads;
import io.samdev.spinheads.util.Chat;
import io.samdev.spinheads.util.CommandType;
import io.samdev.spinheads.util.UtilEntity;
import io.samdev.spinheads.util.UtilServer;
import io.samdev.spinheads.util.UtilString;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class HeadManager
{
    private final SpinHeads plugin;

    public HeadManager(SpinHeads plugin)
    {
        this.plugin = plugin;
        this.spinRate = (float) plugin.getConfig().getDouble("spin_rate");

        loadHeadData();

        UtilServer.registerListener(new InteractListener(plugin));

        Bukkit.getScheduler().runTaskTimer(plugin, this::spinHeads, 0L, 1L);
    }

    private final float spinRate;
    private final Set<HeadData> heads = new HashSet<>();

    private void spinHeads()
    {
        plugin.getDataManager().getTrackedHeads().forEach(trackedHead ->
        {
            Entity headEntity = trackedHead.getCachedEntity();

            if (headEntity == null)
            {
                headEntity = UtilEntity.getEntityFromId(trackedHead.getHeadEntity(), trackedHead.getLocation().getWorld());
                trackedHead.setCachedEntity(headEntity);
            }

            if (headEntity != null)
            {
                Location location = headEntity.getLocation().clone();
                location.setYaw(location.getYaw() + spinRate);

                headEntity.teleport(location);
            }
        });
    }

    private void loadHeadData()
    {
        ConfigurationSection section = plugin.getConfig().getConfigurationSection("heads");

        for (String head : section.getKeys(false))
        {
            ConfigurationSection headSection = section.getConfigurationSection(head);

            String player = headSection.getString("player");

            List<String> hologram = headSection.getStringList("hologram");
            hologram.replaceAll(Chat::colour);

            Map<CommandType, String> commands = UtilString.parseCommands(headSection.getStringList("commands"));

            heads.add(new HeadData(head, player, hologram, commands));
        }
    }

    public HeadData getHead(String input)
    {
        for (HeadData head : heads)
        {
            if (head.getName().equalsIgnoreCase(input))
            {
                return head;
            }
        }

        return null;
    }
}
