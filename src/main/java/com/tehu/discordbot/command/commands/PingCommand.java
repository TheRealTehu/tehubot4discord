package com.tehu.discordbot.command.commands;

import com.tehu.discordbot.command.CommandContext;
import com.tehu.discordbot.command.ICommand;
import com.tehu.discordbot.command.VeryBadDesign;
import net.dv8tion.jda.api.JDA;

public class PingCommand implements ICommand {

    @Override
    public void handle(CommandContext ctx) {
        JDA jda = ctx.getEvent().getJDA();

        jda.getRestPing()
                .queue((ping) -> ctx.getEvent().getChannel()
                        .sendMessageFormat("Reset ping %s ms\nWS: %s ms", ping, jda.getGatewayPing()).queue());
    }

    @Override
    public String getName() {
        return "ping";
    }

    @Override
    public String getHelp(CommandContext ctx) {
        return "Shows the current ping from the BOT to the Discord server.\n" +
                "Usage: `" + VeryBadDesign.PREFIXES.get(ctx.getGuild().getIdLong()) + "ping`";
    }
}
