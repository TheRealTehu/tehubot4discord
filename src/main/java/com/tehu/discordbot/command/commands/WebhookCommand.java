package com.tehu.discordbot.command.commands;

import club.minnced.discord.webhook.WebhookClient;
import club.minnced.discord.webhook.WebhookClientBuilder;
import club.minnced.discord.webhook.send.WebhookMessageBuilder;
import com.tehu.discordbot.Config;
import com.tehu.discordbot.command.CommandContext;
import com.tehu.discordbot.command.ICommand;
import com.tehu.discordbot.command.VeryBadDesign;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;

import java.util.List;

public class WebhookCommand implements ICommand {

    private final WebhookClient client;

    public WebhookCommand() {
        WebhookClientBuilder builder = new WebhookClientBuilder(Config.get("webhook_url")); // or id, token
        builder.setThreadFactory((job) -> {
            Thread thread = new Thread(job);
            thread.setName("Webhook-Thread");
            thread.setDaemon(true);
            return thread;
        });

        client = builder.build();
    }

    @Override
    public void handle(CommandContext ctx) {
        final List<String> args = ctx.getArgs();
        final MessageChannel channel = ctx.getEvent().getChannel();

        if(args.isEmpty()){
            channel.sendMessage("Missing arguments!").queue();
            channel.sendMessage(getHelp(ctx)).queue();
            return;
        }

        final User user = ctx.getEvent().getAuthor();

        WebhookMessageBuilder builder = new WebhookMessageBuilder()
                .setUsername(ctx.getEvent().getMember().getEffectiveName())
                .setAvatarUrl(user.getEffectiveAvatarUrl().replaceFirst("gif", "png") + "?size=512")
                .setContent(String.join(" ", args));

        client.send(builder.build());


    }

    @Override
    public String getName() {
        return "webhook";
    }

    @Override
    public String getHelp(CommandContext ctx) {
        return "Send a webhook message as your name\n" +
                "Usage: `" + VeryBadDesign.PREFIXES.get(ctx.getGuild().getIdLong()) + "webhook [message]`";
    }
}
