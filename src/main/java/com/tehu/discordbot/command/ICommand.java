package com.tehu.discordbot.command;

import java.util.List;

public interface ICommand {
    void handle(CommandContext ctx);
    String getName();

    String getHelp(CommandContext ctx);

    default List<String> getAliases() {
        return List.of();
    }
}
