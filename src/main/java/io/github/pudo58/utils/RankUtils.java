package io.github.pudo58.utils;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONObject;

public class RankUtils {
    private static final OkHttpClient client = new OkHttpClient();

    public static void currentRank(MessageReceivedEvent event) {
        String url = "https://api.lobobot.com/box/token/jfKPn6rV1QbVTD7o"; // Thay đúng URL API bạn sniff được

        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                event.getChannel().sendMessage("Không thể lấy thông tin rank.").queue();
                return;
            }

            JSONObject json = new JSONObject(response.body().string());

            JSONObject riotData = json.getJSONObject("riotData");
            String tier = riotData.getString("tier");
            String rank = riotData.getString("rank");
            int lp = riotData.getInt("leaguePoints");
            int wins = riotData.getInt("wins");
            int losses = riotData.getInt("losses");

            String reply = "**Rank:** " + tier + " " + rank + " - " + lp + " LP\n"
                    + "**Thắng:** " + wins + " | **Thua:** " + losses;

            event.getChannel().sendMessage(reply).queue();

        } catch (Exception e) {
            e.printStackTrace();
            event.getChannel().sendMessage("Đã xảy ra lỗi khi gọi API.").queue();
        }
    }
}
