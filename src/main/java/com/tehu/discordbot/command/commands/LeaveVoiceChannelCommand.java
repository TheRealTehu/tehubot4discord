package com.tehu.discordbot.command.commands;

import com.tehu.discordbot.command.CommandContext;
import com.tehu.discordbot.command.ICommand;
import com.tehu.discordbot.command.VeryBadDesign;
import net.dv8tion.jda.api.entities.VoiceChannel;

public class LeaveVoiceChannelCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        VoiceChannel connectedChannel = (VoiceChannel) ctx.getGuild().getSelfMember().getVoiceState().getChannel();
        if (connectedChannel == null) {
            ctx.getEvent().getChannel().sendMessage("I'm not even connected to a channel. Y U BULLY ME? :'(").queue();
            return;
        }
        ctx.getGuild().getAudioManager().closeAudioConnection();
    }

    @Override
    public String getName() {
        return "leave";
    }

    @Override
    public String getHelp(CommandContext ctx) {
        return "Bot will leave the voice channel it is currently in.\n" +
                "Usage: `" + VeryBadDesign.PREFIXES.get(ctx.getGuild().getIdLong()) + "leave`";
    }
}
