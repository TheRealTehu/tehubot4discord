package com.tehu.discordbot;

import com.tehu.discordbot.command.VeryBadDesign;
import com.tehu.discordbot.database.DatabaseManager;
import me.duncte123.botcommons.BotCommons;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.Event;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.guild.GenericGuildEvent;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class Listener extends ListenerAdapter {

    private final String BOTCHANNELID;
    private final Logger LOGGER;
    private final CommandManager manager;

    public Listener(String botChannelID) {
        this.BOTCHANNELID = botChannelID;
        LOGGER = LoggerFactory.getLogger(Listener.class);
        manager = new CommandManager();
    }

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        LOGGER.info("{} is ready", event.getJDA().getSelfUser().getAsTag());
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (isBot(event)) {
            return;
        }

        User user = event.getAuthor();

        LOGGER.info("Message from {}: {}", user.getName(), event.getMessage().getContentDisplay());

        if (notGuildChannel(event)) {
            event.getChannel().sendMessage("Only talk to me in my channel, peasant!").queue();
            return;
        }

        final long guildId = event.getGuild().getIdLong();
        String prefix = VeryBadDesign.PREFIXES.computeIfAbsent(guildId, DatabaseManager.INSTANCE::getPrefix);
        String message = event.getMessage().getContentRaw().toLowerCase();

        if (!isCommand(event, prefix) && !notBotChannel(event)) {
            event.getChannel().sendMessage("The current botprefix is: " + prefix).queue();
        }

        if (!isCommand(event, prefix)) {
            return;
        }

        if (notBotChannel(event)) {
            event.getChannel().sendMessage("Only talk to me in my channel, peasant!").queue();
            return;
        }

        if (message.equals(prefix + "shutdown") && event.getMember().hasPermission(Permission.ADMINISTRATOR)) {
            shutDown(event);
            return;
        }

        if (message.startsWith(prefix)) {
            manager.handle(event, prefix);
        }
    }

    private boolean notBotChannel(MessageReceivedEvent event) {
        return !event.getChannel().getId().equals(BOTCHANNELID);
    }

    private boolean isCommand(MessageReceivedEvent event, String prefix) {
        return event.getMessage().getContentRaw().startsWith(prefix);
    }

    private boolean notGuildChannel(MessageReceivedEvent event) {
        return event.getChannelType().getId() != 0;
    }

    private void shutDown(MessageReceivedEvent event) {
        LOGGER.info("Shutting down");
        event.getJDA().shutdown();
        BotCommons.shutdown(event.getJDA());
    }

    private boolean isBot(MessageReceivedEvent event) {
        return event.getAuthor().isBot() || event.isWebhookMessage();
    }

    @Override
    public void onGuildMemberJoin(@NotNull GuildMemberJoinEvent event) {
        saluteMember(event.getGuild(), event.getMember(), "join");
    }

    @Override
    public void onGuildMemberRemove(@NotNull GuildMemberRemoveEvent event) {
        saluteMember(event.getGuild(), event.getMember(), "leave");
    }

    private void saluteMember(@NotNull Guild guild, Member member, String way) {
        System.out.println("I found a moving person");
        final List<TextChannel> txtChannels = guild.getTextChannelsByName("general", true);

        if (txtChannels.isEmpty()) {
            System.out.println("didnt find text channel");
            return;
        }

        final TextChannel theOne = txtChannels.get(0);

        String message = "";

        if (way.equals("join")) {
            message = String.format("Üdv-ü %s a %s szerveren!",
                    member.getUser().getAsTag(), guild.getName());
        } else if (way.equals("leave")) {
            message = String.format("Byeee %s!", member.getUser().getAsTag());
        }


        theOne.sendMessage(message).queue();
    }

    @Override
    public void onGuildJoin(@NotNull GuildJoinEvent event) {
        if(DatabaseManager.INSTANCE.containsGuild(event.getGuild().getIdLong())){
            //Greeting maybe
            return;
        } else {
            firstTimeSetUp(event.getGuild());
        }
    }

    private void firstTimeSetUp(Guild guild) {
    }
}
