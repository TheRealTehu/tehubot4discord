package com.tehu.discordbot.command.commands;

import com.tehu.discordbot.command.CommandContext;
import com.tehu.discordbot.command.ICommand;
import com.tehu.discordbot.command.VeryBadDesign;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.managers.AudioManager;

public class JoinVoiceChannelCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        if (!ctx.getGuild().getSelfMember().hasPermission((GuildChannel) ctx.getEvent().getChannel(), Permission.VOICE_CONNECT)) {
            ctx.getEvent().getChannel().sendMessage("I have no permission to do that!").queue();
            return;
        }

        VoiceChannel connectedChannel = (VoiceChannel) ctx.getEvent().getMember().getVoiceState().getChannel();

        if (connectedChannel == null) {
            ctx.getEvent().getChannel().sendMessage("You are not connected to a voice channel! Duuuude. Wtf...").queue();
            return;
        }
        AudioManager audioManager = ctx.getGuild().getAudioManager();
        audioManager.openAudioConnection(connectedChannel);
        ctx.getEvent().getChannel().sendMessage("Connected to the voice channel!").queue();
    }

    @Override
    public String getName() {
        return "join";
    }

    @Override
    public String getHelp(CommandContext ctx) {
        return "Bot will join your voice channel.\n" +
                "Usage: `" + VeryBadDesign.PREFIXES.get(ctx.getGuild().getIdLong()) + "join`";
    }
}
