package com.tehu.discordbot.command.commands.admin;

import com.tehu.discordbot.command.CommandContext;
import com.tehu.discordbot.command.ICommand;
import com.tehu.discordbot.command.VeryBadDesign;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;

import java.util.List;

public class KickCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        final MessageChannel channel = ctx.getEvent().getChannel();
        final Message message = ctx.getEvent().getMessage();
        final Member member = ctx.getEvent().getMember();
        final List<String> args = ctx.getArgs();

        if(args.size() < 2 || message.getMentionedMembers().isEmpty()){
            channel.sendMessage("Missing arguments!").queue();
            channel.sendMessage(getHelp(ctx)).queue();
            return;
        }

        final Member target =  message.getMentionedMembers().get(0);
        final Member self = message.getGuild().getSelfMember();

        if(!member.canInteract(target) || !member.hasPermission(Permission.KICK_MEMBERS)){
            channel.sendMessage("You don't have permission for that!").queue();
        } else if(!self.canInteract(target) || !self.hasPermission(Permission.KICK_MEMBERS)){
            channel.sendMessage("I don't have permission for that!").queue();
        } else {
            kickUser(ctx, channel, target, message, args);
        }

    }

    private void kickUser(CommandContext ctx, MessageChannel channel, Member target, Message message, List<String> args) {

        final String reason = String.join(" ", args.subList(1, args.size()));

        if(target == null){
            channel.sendMessage("No such person on the server!").queue();
        } else {
            ctx.getGuild()
                    .kick(target, reason)
                    .reason(reason)
                    .queue();
        }
    }

    @Override
    public String getName() {
        return "kick";
    }

    @Override
    public String getHelp(CommandContext ctx) {
        return "Kick a user off the server. (Needs permission)\n" +
                "Usage: `" + VeryBadDesign.PREFIXES.get(ctx.getGuild().getIdLong()) + "kick <@user> <reason>`";
    }
}
