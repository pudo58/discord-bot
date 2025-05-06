package io.github.pudo58.bot.noxus;

import io.github.pudo58.utils.RankUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;

public class CommandListener extends ListenerAdapter {
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        String msg = event.getMessage().getContentRaw();

        if (event.getAuthor().isBot()) return;

        switch (msg) {
            case "!rank":
                RankUtils.currentRank(event);
                break;
            case "!hello":
                event.getChannel().sendMessage("Xin chào " + event.getAuthor().getAsMention()).queue();
                break;
            case "!info": {
                EmbedBuilder embed = new EmbedBuilder()
                        .setTitle("Thông tin bot")
                        .setDescription("Bot Java dùng JDA")
                        .setColor(Color.CYAN)
                        .addField("Tác giả", "LÃ VĂN THỌ 😎", false)
                        .addField("Ngôn ngữ", "Java + JDA", false)
                        .setFooter("Made with ❤️ by you");
                event.getChannel().sendMessageEmbeds(embed.build()).queue();
                break;
            }
        }
    }
}