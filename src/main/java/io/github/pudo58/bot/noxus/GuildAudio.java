package io.github.pudo58.bot.noxus;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;

public class GuildAudio {
    public final AudioPlayer player;
    public final TrackScheduler scheduler;
    private final AudioPlayerSendHandler sendHandler;

    public GuildAudio(AudioPlayerManager manager) {
        this.player = manager.createPlayer();
        this.scheduler = new TrackScheduler(player);
        this.player.addListener(scheduler);
        this.sendHandler = new AudioPlayerSendHandler(player);
    }

    public AudioPlayerSendHandler getSendHandler() {
        return sendHandler;
    }
}