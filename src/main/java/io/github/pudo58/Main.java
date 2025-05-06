package io.github.pudo58;

import io.github.pudo58.bot.introduction.BadWordFilter;
import io.github.pudo58.bot.introduction.CommandListener;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;

public class Main {
    public static void main(String[] args) {
        String token = "MTM2OTE0MTY1MjExOTM1OTUzOA.G_B3SP.HcLCTeuAeVDcnyeEm2KQiTCbMtZtZJtiO2bnDU";

        JDABuilder.createDefault(token)
                .enableIntents(
                        GatewayIntent.GUILD_MESSAGES,
                        GatewayIntent.MESSAGE_CONTENT,
                        GatewayIntent.GUILD_MEMBERS
                )
                .addEventListeners(new CommandListener())
                .addEventListeners( new BadWordFilter())
                .build();
    }
}
