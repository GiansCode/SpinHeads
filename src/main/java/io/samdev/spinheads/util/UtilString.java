package io.samdev.spinheads.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class UtilString
{
    private UtilString() {}

    public static String formatMessage(String value, Object... params)
    {
        if (params.length == 0)
        {
            return value;
        }

        assert params.length % 2 == 0 : "Message params unbalanced";

        String message = value;

        for (int i = 0; i < params.length - 1; i += 2)
        {
            message = message.replace("{" + params[i] + "}", String.valueOf(params[i + 1]));
        }

        return message;
    }

    private static String join(String[] args, int startIndex, String delimiter)
    {
        StringBuilder builder = new StringBuilder();

        for (int i = startIndex; i < args.length; i++)
        {
            builder.append(args[i]).append(delimiter);
        }

        return builder.toString().trim();
    }

    public static Map<CommandType, String> parseCommands(List<String> rawCommands)
    {
        Map<CommandType, String> commands = new HashMap<>();

        for (String rawCommand : rawCommands)
        {
            String[] split = rawCommand.split(" ");

            try
            {
                CommandType type = CommandType.valueOf(split[0]
                    .toUpperCase()
                    .replace("[", "")
                    .replace("]", "")
                );
                String command = Chat.colour(join(split, 1, " "));

                commands.put(type, command);
            }
            catch (IllegalArgumentException ex)
            {
                UtilServer.getLogger().severe("Invalid command type: " + split[0]);
            }
        }

        return commands;
    }
}