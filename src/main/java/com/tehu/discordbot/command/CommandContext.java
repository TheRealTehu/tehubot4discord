package com.tehu.discordbot.command;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.List;

public class CommandContext {
    private final MessageReceivedEvent event;
    private final List<String> args;

    public CommandContext(MessageReceivedEvent event, List<String> args) {
        this.event = event;
        this.args = args;
    }

    public Guild getGuild(){
        return this.event.getGuild();
    }

    public MessageReceivedEvent getEvent() {
        return event;
    }

    public List<String> getArgs() {
        return args;
    }
}
