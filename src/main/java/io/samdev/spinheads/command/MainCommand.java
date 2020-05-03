package io.samdev.spinheads.command;

import io.samdev.spinheads.SpinHeads;
import io.samdev.spinheads.data.TrackedHead;
import io.samdev.spinheads.head.HeadData;
import io.samdev.spinheads.util.Message;
import io.samdev.spinheads.util.Permission;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.List;

public class MainCommand implements CommandExecutor
{
    private final SpinHeads plugin;

    public MainCommand(SpinHeads plugin)
    {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        handleArgs(sender, args);
        return true;
    }

    private void handleArgs(CommandSender sender, String[] args)
    {
        if (!Permission.COMMAND_MAIN.has(sender))
        {
            return;
        }

        if (args.length == 0)
        {
            sendUsage(sender);
            return;
        }

        switch (args[0].toLowerCase())
        {
            case "create":
                if (Permission.COMMAND_CREATE.has(sender) && playerCheck(sender))
                {
                    handleCreate((Player) sender, args);
                }
                break;

            case "list":
                if (Permission.COMMAND_LIST.has(sender) && playerCheck(sender))
                {
                    handleList((Player) sender, args);
                }
                break;

            case "delete":
                if (Permission.COMMAND_DELETE.has(sender) && playerCheck(sender))
                {
                    handleDelete((Player) sender, args);
                }
                break;

            case "reload":
                if (Permission.COMMAND_RELOAD.has(sender))
                    handleReload(sender);
                break;
                
            default:
                sendUsage(sender);
                break;
        }
    }

    private void handleCreate(Player player, String[] args)
    {
        if (args.length != 2)
        {
            Message.COMMAND_CREATE_USAGE.send(player);
            return;
        }

        HeadData head = plugin.getHeadManager().getHead(args[1]);

        if (head == null)
        {
            Message.INVALID_HEAD.send(player);
            return;
        }

        plugin.getDataManager().addHead(head.spawnHead(player.getLocation()));
        player.teleport(player.getLocation().clone().add(0.0D, 1.0D, 0.0D));
    }

    private void handleList(Player player, String[] args)
    {
        if (args.length != 2)
        {
            Message.COMMAND_LIST_USAGE.send(player);
            return;
        }

        int radius = parseInt(args[1], player);

        if (radius == -1)
        {
            return;
        }

        List<TrackedHead> entities = plugin.getDataManager().getEntities(player, radius);

        Message.COMMAND_LIST_HEADER.send(player, "count", entities.size());

        for (int i = 0; i < entities.size(); i++)
        {
            TrackedHead trackedHead = entities.get(i);

            Message.COMMAND_LIST_FORMAT.send(player, "number", i + 1, "head_type", trackedHead.getHeadData().getName());
        }
    }

    private void handleDelete(Player player, String[] args)
    {
        if (args.length != 2)
        {
            Message.COMMAND_DELETE_USAGE.send(player);
            return;
        }

        int radius = parseInt(args[1], player);

        if (radius == -1)
        {
            return;
        }

        int count = 0;

        for (Entity entity : player.getNearbyEntities(radius, radius, radius))
        {
            if (plugin.getDataManager().deleteEntity(entity))
            {
                count++;
            }
        }

        Message.COMMAND_DELETE_EXECUTED.send(player, "count", count);
    }

    private void handleReload(CommandSender sender) {
        plugin.reloadConfig();
        plugin.getHeadManager().loadHeadData();
        plugin.getDataManager().save();
        plugin.getDataManager().loadDataFile();
        
        Message.COMMAND_RELOAD_EXECUTED.send(sender);
    }
    
    private int parseInt(String input, Player player)
    {
        int value;

        try
        {
            value = Integer.parseInt(input);

            if (value < 1)
            {
                Message.BAD_NUMBER.send(player);
                value = -1;
            }
        }
        catch (NumberFormatException ex)
        {
            Message.INVALID_NUMBER.send(player);
            value = -1;
        }

        return value;
    }

    private void sendUsage(CommandSender sender)
    {
        Message.COMMAND_MAIN_USAGE.send(sender);
    }

    private boolean playerCheck(CommandSender sender)
    {
        if (!(sender instanceof Player))
        {
            Message.NO_CONSOLE.send(sender);
            return false;
        }

        return true;
    }
}
