package io.samdev.spinheads.util;

import io.samdev.spinheads.SpinHeads;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public final class UtilServer
{
    private UtilServer() {}

    private static final SpinHeads PLUGIN = JavaPlugin.getPlugin(SpinHeads.class);

    public static SpinHeads getPlugin()
    {
        return PLUGIN;
    }

    public static Logger getLogger()
    {
        return PLUGIN.getLogger();
    }

    public static void registerListener(Listener listener)
    {
        Bukkit.getPluginManager().registerEvents(listener, PLUGIN);
    }

    public static void runAsync(Runnable runnable)
    {
        Bukkit.getScheduler().runTaskAsynchronously(PLUGIN, runnable);
    }

    public static void runSync(Runnable runnable)
    {
        Bukkit.getScheduler().runTask(PLUGIN, runnable);
    }
}
