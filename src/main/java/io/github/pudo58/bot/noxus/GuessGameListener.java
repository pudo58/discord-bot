package io.github.pudo58.bot.noxus;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

public class GuessGameListener extends ListenerAdapter {
    private final ConcurrentHashMap<Long, Integer> userTargetMap = new ConcurrentHashMap<>();
    private final Random random = new Random();

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        String[] args = event.getMessage().getContentRaw().split(" ");
        if (!args[0].equalsIgnoreCase("!guess")) return;

        long userId = event.getAuthor().getIdLong();

        // Nếu chưa có số để đoán, bot tạo một số mới
        userTargetMap.putIfAbsent(userId, random.nextInt(100) + 1);

        if (args.length != 2) {
            event.getChannel().sendMessage("Dùng đúng cú pháp: `!guess [số từ 1-100]`").queue();
            return;
        }

        try {
            int guess = Integer.parseInt(args[1]);
            int target = userTargetMap.get(userId);

            if (guess == target) {
                event.getChannel().sendMessage("🎉 Đúng rồi! Bạn đoán trúng số **" + target + "**").queue();
                userTargetMap.remove(userId); // reset
            } else if (guess < target) {
                event.getChannel().sendMessage("🔽 Số bạn đoán **nhỏ hơn**").queue();
            } else {
                event.getChannel().sendMessage("🔼 Số bạn đoán **lớn hơn**").queue();
            }

        } catch (NumberFormatException e) {
            event.getChannel().sendMessage("Hãy nhập số hợp lệ.").queue();
        }
    }
}
