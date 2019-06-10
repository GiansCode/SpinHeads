package io.samdev.spinheads.util;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.function.BiConsumer;

public enum CommandType
{
    MESSAGE(
        Player::sendMessage
    ),
    CONSOLE(
        (player, command) -> Bukkit.dispatchCommand(
            Bukkit.getConsoleSender(), command.replace("%player%", player.getName())
        )
    ),
    PLAYER(
        Player::performCommand
    )
    ;

    private final BiConsumer<Player, String> consumer;

    CommandType(BiConsumer<Player, String> consumer)
    {
        this.consumer = consumer;
    }

    public void execute(Player player, String command)
    {
        consumer.accept(player, command);
    }
}
