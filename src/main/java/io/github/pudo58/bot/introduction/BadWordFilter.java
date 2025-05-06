package io.github.pudo58.bot.introduction;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class BadWordFilter extends ListenerAdapter {
    private final String[] bannedWords = {"địt", "fuck", "dm", "cc", "shit"};

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        String content = event.getMessage().getContentRaw().toLowerCase();
        if (event.getAuthor().isBot()) return;

        for (String bad : bannedWords) {
            if (content.contains(bad)) {
                event.getMessage().delete().queue();
                event.getChannel().sendMessage(event.getAuthor().getAsMention() + ", vui lòng không dùng từ ngữ không phù hợp!")
                        .queue();
                break;
            }
        }
    }
}

