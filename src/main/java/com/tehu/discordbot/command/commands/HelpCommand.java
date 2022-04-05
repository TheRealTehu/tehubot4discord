package com.tehu.discordbot.command.commands;

import com.tehu.discordbot.CommandManager;
import com.tehu.discordbot.Config;
import com.tehu.discordbot.command.CommandContext;
import com.tehu.discordbot.command.ICommand;
import com.tehu.discordbot.command.VeryBadDesign;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.List;

public class HelpCommand implements ICommand {

    private final CommandManager manager;

    public HelpCommand(CommandManager manager) {
        this.manager = manager;
    }

    @Override
    public void handle(CommandContext ctx) {
        List<String> args = ctx.getArgs();
        TextChannel channel = ctx.getEvent().getTextChannel();

        if(args.isEmpty()){
            listCommands(channel, ctx);
            return;
        }

        String search = args.get(0);
        ICommand command = manager.getCommand(search);

        if(command == null){
            channel.sendMessage("Didn't found command: " + search).queue();
        } else {
            channel.sendMessage(command.getHelp(ctx)).queue();
        }

    }

    private void listCommands(TextChannel channel, CommandContext ctx) {
        StringBuilder sb = new StringBuilder();
        String prefix = VeryBadDesign.PREFIXES.get(ctx.getGuild().getIdLong());
        sb.append("List of commands: \n");

        manager.getCommands().stream().map(ICommand::getName).forEach(
                (it) -> sb.append("`")
                        .append(prefix)
                        .append(it)
                        .append("`\n")
        );

        channel.sendMessage(sb.toString()).queue();
    }

    @Override
    public String getName() {
        return "help";
    }

    @Override
    public String getHelp(CommandContext ctx) {
        return "Shows a list with commands in the bot.\n" +
                "Usage: `" + VeryBadDesign.PREFIXES.get(ctx.getGuild().getIdLong()) + "help [command]`";
    }

    @Override
    public List<String> getAliases() {
        return List.of("commands", "cmd", "cmds", "commandlist", "parancsok", "utasítások", "lista");
    }
}
