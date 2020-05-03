package io.samdev.spinheads.util;

import org.bukkit.command.CommandSender;

public enum Permission
{
    COMMAND_MAIN("command.main"),
    COMMAND_CREATE("command.create"),
    COMMAND_LIST("command.list"),
    COMMAND_DELETE("command.delete"),
    COMMAND_RELOAD("command.reload")
    ;

    private final String node;

    Permission(String node)
    {
        this.node = "spinheads." + node;
    }

    public boolean has(CommandSender sender)
    {
        return has(sender, true);
    }

    public boolean has(CommandSender sender, boolean inform)
    {
        if (!sender.hasPermission(node))
        {
            if (inform)
            {
                Message.NO_PERMISSION.send(sender);
            }

            return false;
        }

        return true;
    }
}
