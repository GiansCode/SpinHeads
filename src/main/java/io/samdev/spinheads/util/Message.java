package io.samdev.spinheads.util;

import io.samdev.spinheads.SpinHeads;
import org.bukkit.command.CommandSender;

import java.util.List;

public enum Message
{
    NO_PERMISSION,
    NO_CONSOLE,

    COMMAND_MAIN_USAGE,

    COMMAND_CREATE_USAGE,

    COMMAND_LIST_USAGE,
    COMMAND_LIST_HEADER,
    COMMAND_LIST_FORMAT,

    COMMAND_DELETE_USAGE,
    COMMAND_DELETE_EXECUTED,

    COMMAND_RELOAD_EXECUTED,
    
    INVALID_NUMBER,
    BAD_NUMBER,

    INVALID_HEAD
    ;

    private String value = name();

    public void send(CommandSender sender, Object... params)
    {
        if (value != null)
        {
            sender.sendMessage(UtilString.formatMessage(value, params));
        }
    }

    public void broadcast(Object... params)
    {
        Chat.broadcast(UtilString.formatMessage(value, params));
    }

    @SuppressWarnings("unchecked")
    public static void init(SpinHeads plugin)
    {
        for (Message message : values())
        {
            Object object = plugin.getConfig().get("messages." + message.name().toLowerCase());

            if (object == null)
            {
                plugin.getLogger().severe("Value missing for message " + message.name());
                continue;
            }

            String value =
                object instanceof String ? (String) object :
                    object instanceof List ? String.join("\n", (List<String>) object) :
                    null;

            if (value == null)
            {
                plugin.getLogger().severe("Invalid data type for message " + message.name());
                continue;
            }

            message.value = Chat.colour(value);
        }
    }
}