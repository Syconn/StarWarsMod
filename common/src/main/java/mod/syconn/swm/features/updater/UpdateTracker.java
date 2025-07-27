package mod.syconn.swm.features.updater;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import mod.syconn.swm.utils.Constants;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class UpdateTracker {

    private static final String LINK = "https://raw.githubusercontent.com/Syconn/Syconn-Mod-Updates/refs/heads/main/StarWarsMod.json";

    public static void main(String[] args) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(LINK)).GET().build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                JsonObject json = new Gson().fromJson(response.body(), JsonObject.class);
                System.out.println("Full JSON:\n" + json);
            } else Constants.LOG.warn("Unable to load Json Data");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}