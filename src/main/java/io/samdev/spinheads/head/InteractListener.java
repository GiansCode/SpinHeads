package io.samdev.spinheads.head;

import io.samdev.spinheads.SpinHeads;
import io.samdev.spinheads.data.TrackedHead;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class InteractListener implements Listener
{
    private final SpinHeads plugin;

    InteractListener(SpinHeads plugin)
    {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event)
    {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK)
        {
            return;
        }

        TrackedHead trackedHead = plugin.getDataManager().getTrackedHead(event.getClickedBlock().getLocation());

        if (trackedHead != null)
        {
            trackedHead.getHeadData().executeCommands(event.getPlayer());
        }
    }
    
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        TrackedHead trackedHead = plugin.getDataManager().getTrackedHead(event.getBlock().getLocation());
        if (trackedHead != null)
            event.setCancelled(true);
    }
}
