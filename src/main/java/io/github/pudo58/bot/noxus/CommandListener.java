package io.github.pudo58.bot.noxus;

import io.github.pudo58.utils.RankUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;
import java.util.logging.Logger;

import static io.github.pudo58.constant.RankConstant.BRONZE;

public class CommandListener extends ListenerAdapter {
    private static final Logger log = Logger.getLogger(CommandListener.class.getName());

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        Role role = event.getGuild().getRolesByName(BRONZE, true).get(0);
        event.getGuild().addRoleToMember(event.getMember(), role).queue();
        log.info("Add rank: " + BRONZE + "to user: " + event.getMember());
        String username = event.getUser().getName().toLowerCase();
        String[] suspiciousKeywords = {"admin", "hack", "tool", "script", "mod"};

        // Gửi welcome embed vào kênh #chào-mừng
        TextChannel welcomeChannel = event.getGuild().getTextChannelsByName("chào-mừng", true).get(0);

        EmbedBuilder embed = new EmbedBuilder()
                .setColor(Color.GREEN)
                .setTitle("🎉 Chào mừng " + event.getUser().getAsTag() + "!")
                .setDescription("Chúc bạn vui vẻ trong server **" + event.getGuild().getName() + "**!\nHãy xem qua các kênh #lịch-stream và #thông-báo.")
                .setThumbnail(event.getUser().getEffectiveAvatarUrl());

        welcomeChannel.sendMessageEmbeds(embed.build()).queue();

        for (String keyword : suspiciousKeywords) {
            if (username.contains(keyword)) {
                TextChannel modChannel = event.getGuild().getTextChannelsByName("mod-log", true).get(0);
                modChannel.sendMessage("⚠️ Tên thành viên mới `" + username + "` chứa từ khóa nghi vấn: `" + keyword + "`.\n<@&MOD_ROLE_ID>").queue();
                break;
            }
        }
    }

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