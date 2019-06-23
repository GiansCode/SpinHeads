package io.samdev.spinheads.util;

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
}