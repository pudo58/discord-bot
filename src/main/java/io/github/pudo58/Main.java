package io.github.pudo58;

import io.github.pudo58.bot.introduction.BadWordFilter;
import io.github.pudo58.bot.introduction.CommandListener;
import io.github.pudo58.constant.BotConstant;
import io.github.pudo58.utils.EnvUtils;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;

public class Main {
    public static void main(String[] args) {
        String tokenIntroduction = EnvUtils.getEnv(BotConstant.BOT_INTRODUCTION_KEY);
        System.out.println(tokenIntroduction);

        JDABuilder.createDefault(tokenIntroduction)
                .enableIntents(
                        GatewayIntent.GUILD_MESSAGES,
                        GatewayIntent.MESSAGE_CONTENT,
                        GatewayIntent.GUILD_MEMBERS
                )
                .addEventListeners(new CommandListener())
                .addEventListeners( new BadWordFilter())
                .build();

        String tokenNoxusBot = EnvUtils.getEnv(BotConstant.BOT_INTRODUCTION_KEY);
        System.out.println(tokenNoxusBot);

        JDABuilder.createDefault(tokenNoxusBot)
                .enableIntents(
                        GatewayIntent.GUILD_MESSAGES,
                        GatewayIntent.MESSAGE_CONTENT,
                        GatewayIntent.GUILD_MEMBERS
                )
                .addEventListeners(new io.github.pudo58.bot.noxus.CommandListener())
                .addEventListeners( new BadWordFilter())
                .build();
    }
}
