package io.github.pudo58.bot.noxus;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.source.http.HttpAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.soundcloud.SoundCloudAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import io.github.pudo58.utils.RankUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.entities.channel.unions.AudioChannelUnion;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
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
        if (msg.startsWith("!play")) {
            String url = msg.substring(6);
            playMusic(event, url);
        }
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

    private final AudioPlayerManager playerManager = new DefaultAudioPlayerManager();
    private final Map<Long, GuildAudio> musicManagers = new HashMap<>();

    public CommandListener() {
        playerManager.registerSourceManager(new YoutubeAudioSourceManager());
        playerManager.registerSourceManager(SoundCloudAudioSourceManager.createDefault());
        playerManager.registerSourceManager(new HttpAudioSourceManager());
        playerManager.getConfiguration().setFilterHotSwapEnabled(true);
        AudioSourceManagers.registerRemoteSources(playerManager);
    }

    private synchronized GuildAudio getGuildAudio(Guild guild) {
        return musicManagers.computeIfAbsent(guild.getIdLong(), (guildId) -> {
            GuildAudio manager = new GuildAudio(playerManager);
            guild.getAudioManager().setSendingHandler(manager.getSendHandler());
            return manager;
        });
    }

    public void playMusic(MessageReceivedEvent event, String url) {
        Guild guild = event.getGuild();
        AudioChannelUnion channel = event.getMember().getVoiceState().getChannel();
        if (channel instanceof VoiceChannel) {
            VoiceChannel voiceChannel = (VoiceChannel) channel;
            GuildAudio guildAudio = getGuildAudio(guild);

            guild.getAudioManager().openAudioConnection(voiceChannel);

            playerManager.loadItem(url, new AudioLoadResultHandler() {
                @Override
                public void trackLoaded(AudioTrack track) {
                    guildAudio.scheduler.queue(track);
                    event.getChannel().sendMessage("Đã thêm vào hàng đợi: " + track.getInfo().title).queue();
                }

                @Override
                public void playlistLoaded(AudioPlaylist playlist) {
                    AudioTrack firstTrack = playlist.getSelectedTrack() != null
                            ? playlist.getSelectedTrack()
                            : playlist.getTracks().get(0);
                    guildAudio.scheduler.queue(firstTrack);
                    event.getChannel().sendMessage("Đã thêm bài đầu tiên từ playlist: " + firstTrack.getInfo().title).queue();
                }

                @Override
                public void noMatches() {
                    event.getChannel().sendMessage("Không tìm thấy bài nào.").queue();
                }

                @Override
                public void loadFailed(FriendlyException exception) {
                    event.getChannel().sendMessage("Lỗi khi tải bài hát: " + exception.getMessage()).queue();
                }
            });
        }
    }

}