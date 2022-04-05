package com.tehu.discordbot.command.commands;

import com.fasterxml.jackson.databind.JsonNode;
import com.tehu.discordbot.Config;
import com.tehu.discordbot.command.CommandContext;
import com.tehu.discordbot.command.ICommand;
import com.tehu.discordbot.command.VeryBadDesign;
import me.duncte123.botcommons.messaging.EmbedUtils;
import me.duncte123.botcommons.web.WebUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageChannel;

public class MemeCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        final MessageChannel channel = ctx.getEvent().getChannel();
        WebUtils.ins.getJSONObject("https://apis.duncte123.me/meme").async((json) -> {
            if(!json.get("success").asBoolean()){
                channel.sendMessage("Something's WRONG here! Try again later.").queue();
                System.out.println(json);
                return;
            }
            final JsonNode data = json.get("data");
            final String title = data.get("title").asText();
            final String url = data.get("url").asText();
            final String image = data.get("image").asText();

            final EmbedBuilder embed = EmbedUtils.embedImageWithTitle(title, url, image);

            ctx.getGuild().getTextChannelById(Config.get("memechannel")).sendMessageEmbeds(embed.build()).queue();
        });
    }

    @Override
    public String getName() {
        return "meme";
    }

    @Override
    public String getHelp(CommandContext ctx) {
        return "Shows random meme\n" +
                "Usage: '" + VeryBadDesign.PREFIXES.get(ctx.getGuild().getIdLong()) + "meme'";
    }
}
