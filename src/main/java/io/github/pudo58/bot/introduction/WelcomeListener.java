package io.github.pudo58.bot.introduction;

import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class WelcomeListener extends ListenerAdapter {
    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        event.getGuild().getTextChannels().stream()
                .filter(TextChannel::canTalk)
                .findFirst().ifPresent(defaultChannel -> defaultChannel.sendMessage("👋 Chào mừng " + event.getMember().getAsMention() + " đến với group Thọ Darius!")
                        .queue());

    }
}