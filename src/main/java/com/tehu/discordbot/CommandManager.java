package com.tehu.discordbot;

import com.tehu.discordbot.command.CommandContext;
import com.tehu.discordbot.command.ICommand;
import com.tehu.discordbot.command.commands.*;
import com.tehu.discordbot.command.commands.admin.KickCommand;
import com.tehu.discordbot.command.commands.admin.SetPrefixCommand;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class CommandManager {
    private final List<ICommand> commands;

    public CommandManager() {
        commands = new ArrayList<>();

        addCommand(new PingCommand());
        addCommand(new HelpCommand(this));
        addCommand(new JoinVoiceChannelCommand());
        addCommand(new LeaveVoiceChannelCommand());
        addCommand(new MemeCommand());
        addCommand(new JokeCommand());
        addCommand(new WebhookCommand());
        addCommand(new InsultCommand());

        addCommand(new KickCommand());
        addCommand(new SetPrefixCommand());
    }

    public List<ICommand> getCommands() {
        return commands;
    }

    public void addCommand(ICommand command){
        boolean nameFound = this.commands.stream().anyMatch((it) -> it.getName().equalsIgnoreCase(command.getName()));

        if(nameFound){
            throw new IllegalArgumentException("Command already exists!");
        }
        commands.add(command);
    }

    @Nullable
    public ICommand getCommand(String search){
        String searchLower = search.toLowerCase();

        for (ICommand c : commands) {
            if(c.getName().equals(searchLower) || c.getAliases().contains(searchLower)){
                return c;
            }
        }
        return null;
    }

    protected void handle(MessageReceivedEvent event, String prefix){
        String[] split = event.getMessage().getContentRaw()
                .replaceFirst("(?i)" + Pattern.quote(prefix), "")
                .split("\\s+");

        String invoke = split[0].toLowerCase();
        ICommand cmd = this.getCommand(invoke);



        if(cmd != null){
            event.getChannel().sendTyping().queue();
            List<String> args = Arrays.asList(split).subList(1, split.length);
            CommandContext ctx = new CommandContext(event, args);

            cmd.handle(ctx);
        } else {
            event.getChannel()
                    .sendMessage("Are you sure about that Champ? Coz I didn't find a command like that.").queue();
            CommandContext ctx = new CommandContext(event, List.of());
            new HelpCommand(this).handle(ctx);
        }
    }
}
