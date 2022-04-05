package com.tehu.discordbot;

import me.duncte123.botcommons.messaging.EmbedUtils;
import me.duncte123.botcommons.web.WebUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;

import javax.security.auth.login.LoginException;

public class Bot {

    public Bot() throws LoginException {
        WebUtils.setUserAgent("Tehubot/TheRealTehu");

        EmbedUtils.setEmbedBuilder(
                () -> new EmbedBuilder()
                        .setColor(0xc21193)
                        .setFooter("Tehu's stuff")
        );

        JDA jda = JDABuilder.createDefault(Config.get("token"), GatewayIntent.getIntents(GatewayIntent.ALL_INTENTS))
                .build();
        jda.addEventListener(new Listener(Config.get("botchannel")));
    }

}
