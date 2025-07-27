package mod.syconn.swm.utils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class UpdateTracker {

    private static final String LINK = "https://raw.githubusercontent.com/Syconn/Syconn-Mod-Updates/refs/heads/main/StarWarsMod.json";

    private final JsonObject jsonData;

    public UpdateTracker() {
        this.jsonData = this.getTrackerData();
    }

    private JsonObject getTrackerData() {
        var client = HttpClient.newHttpClient();
        var request = HttpRequest.newBuilder().uri(URI.create(LINK)).GET().build();

        try {
            var response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) return new Gson().fromJson(response.body(), JsonObject.class);
        } catch (IOException | InterruptedException e) {
            Constants.LOG.error(e.getLocalizedMessage());
        }

        return null;
    }

    public boolean isValid() {
        return this.jsonData != null;
    }

    public String latestVersion(String mcVersion) {
        var version = mcVersion + "-latest";
        if (!jsonData.has("promos") && jsonData.get("promos").getAsJsonObject().has(version)) return "";
        return jsonData.get("promos").getAsJsonObject().get(version).getAsString();
    }

    public String homepageLink() {
        if (!jsonData.has("homepage")) return "";
        return jsonData.get("homepage").getAsString();
    }

    public boolean shouldUpdate(String currentVersion, String mcVersion) {
        if (this.isValid()) return this.isOutdated(currentVersion, this.latestVersion(mcVersion));
        return false;
    }

    private boolean isOutdated(String currentVersion, String latestVersion) {
        String[] currentParts = currentVersion.split("\\.");
        String[] latestParts = latestVersion.split("\\.");

        int maxLength = Math.max(currentParts.length, latestParts.length);

        for (int i = 0; i < maxLength; i++) {
            int current = i < currentParts.length ? Integer.parseInt(currentParts[i]) : 0;
            int latest = i < latestParts.length ? Integer.parseInt(latestParts[i]) : 0;

            if (current < latest) return true;
            if (current > latest) return false;
        }

        return false;
    }
}