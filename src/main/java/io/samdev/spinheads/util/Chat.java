package io.samdev.spinheads.util;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.List;

public final class Chat
{
    private Chat() {}

    public static String colour(String input)
    {
        return ChatColor.translateAlternateColorCodes('&', input);
    }

    public static void broadcast(String message)
    {
        broadcast(new String[] {message});
    }

    public static void broadcast(List<String> messages)
    {
        broadcast(messages.toArray(new String[0]));
    }

    private static void broadcast(String... messages)
    {
        for (Player player : Bukkit.getOnlinePlayers())
        {
            player.sendMessage(messages);
        }
    }
}
