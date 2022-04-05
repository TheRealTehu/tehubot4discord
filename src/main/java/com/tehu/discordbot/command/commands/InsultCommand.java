package com.tehu.discordbot.command.commands;

import com.tehu.discordbot.command.CommandContext;
import com.tehu.discordbot.command.ICommand;
import com.tehu.discordbot.command.VeryBadDesign;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;

import java.util.List;

public class InsultCommand implements ICommand {
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

        if(!member.canInteract(target) || !member.hasPermission(Permission.MESSAGE_SEND)){
            channel.sendMessage("You don't have permission for that!").queue();
        } else {
            insultUser(channel, message.getAuthor(), target, args);
        }
    }

    private void insultUser(MessageChannel mChannel, User self, Member target, List<String> args) {
        User user = target.getUser();
        final String reason = String.join(" ", args.subList(1, args.size()));
        user.openPrivateChannel()
                .flatMap(channel ->
                        channel.sendMessageFormat
                                ("%s thinks you are a duduhead because %s", self.getName(),  reason)).queue();
    }

    @Override
    public String getName() {
        return "insult";
    }

    @Override
    public String getHelp(CommandContext ctx) {
        return "Will send mean message to selected user\n" +
                "Usage: `" + VeryBadDesign.PREFIXES.get(ctx.getGuild().getIdLong()) + "insult <@user> <reason>`";
    }
}
