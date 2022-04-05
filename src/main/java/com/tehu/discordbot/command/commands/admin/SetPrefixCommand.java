package com.tehu.discordbot.command.commands.admin;

import com.tehu.discordbot.command.CommandContext;
import com.tehu.discordbot.command.ICommand;
import com.tehu.discordbot.command.VeryBadDesign;
import com.tehu.discordbot.database.DatabaseManager;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;

import java.util.List;

public class SetPrefixCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        final MessageChannel channel = ctx.getEvent().getChannel();
        final Member member = ctx.getEvent().getMember();
        final List<String> args = ctx.getArgs();

        if (!member.hasPermission(Permission.MANAGE_SERVER)) {
            channel.sendMessage("You cannot do that! Check your permissions").queue();
            return;
        }

        if (args.isEmpty()) {
            channel.sendMessage("Missing arguments!").queue();
            return;
        }

        final String newPrefix = String.join("", args).toLowerCase();
        updatePrefix(ctx.getGuild().getIdLong(), newPrefix);

        channel.sendMessageFormat("New prefix has been set to `%s`", newPrefix).queue();
    }

    @Override
    public String getName() {
        return "setprefix";
    }

    @Override
    public String getHelp(CommandContext ctx) {
        return "Sets the prefix for this server (Admin only)\n" +
                "Usage: `" + VeryBadDesign.PREFIXES.get(ctx.getGuild().getIdLong()) + "setprefix <prefix>`";
    }

    private void updatePrefix(long guildId, String newPrefix) {
        VeryBadDesign.PREFIXES.put(guildId, newPrefix);
        DatabaseManager.INSTANCE.setPrefix(guildId, newPrefix);
    }
}
