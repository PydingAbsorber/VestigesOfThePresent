package com.pyding.vp.util;

import com.google.gson.*;
import com.pyding.vp.VestigesOfThePresent;
import com.pyding.vp.capability.PlayerCapabilityProviderVP;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class LeaderboardUtil {
    public static String topPlayers = "";
    public static String specialPlayers = "";
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final String DATA_URL = "https://raw.githubusercontent.com/PydingAbsorber/VestigesOfThePresent/master/file.json";
    private static volatile String allPlayers = "";
    private static volatile boolean loading = false;
    private static volatile long lastLoadTime = 0L;
    private static final long RELOAD_COOLDOWN_MS = 5 * 60 * 1000L;
    private static final HttpClient HTTP_CLIENT = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(5))
            .build();

    public static String getAll(){
        return allPlayers;
    }

    public static void reloadAsync() {
        long now = System.currentTimeMillis();
        if (loading) {
            return;
        }
        if (now - lastLoadTime < RELOAD_COOLDOWN_MS) {
            return;
        }
        loading = true;
        lastLoadTime = now;
        CompletableFuture.runAsync(() -> {
            try {
                loadNow();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                loading = false;
            }
        });
    }

    public static void forceReloadAsync() {
        if (loading) {
            return;
        }
        loading = true;
        lastLoadTime = System.currentTimeMillis();
        CompletableFuture.runAsync(() -> {
            try {
                loadNow();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                loading = false;
            }
        });
    }

    private static void loadNow() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(DATA_URL))
                .timeout(Duration.ofSeconds(10))
                .header("User-Agent", "Minecraft-Forge-Minions-Mod")
                .GET()
                .build();

        HttpResponse<String> response = HTTP_CLIENT.send(
                request,
                HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8)
        );

        if (response.statusCode() != 200) {
            throw new IOException("Failed to load remote players data. HTTP " + response.statusCode());
        }

        JsonObject json = JsonParser.parseString(response.body()).getAsJsonObject();

        if (json.has("allPlayers")) {
            allPlayers = parseAllPlayers(json.get("allPlayers"));
        }

        if (json.has("topPlayers")) {
            topPlayers = parseStringOrArray(json.get("topPlayers"));
        }

        if (json.has("specialPlayers")) {
            specialPlayers = parseStringOrArray(json.get("specialPlayers"));
        }
    }

    private static String parseAllPlayers(JsonElement element) {
        if (element == null || element.isJsonNull()) {
            return "";
        }
        if (element.isJsonArray()) {
            return GSON.toJson(element.getAsJsonArray());
        }
        if (element.isJsonPrimitive()) {
            return element.getAsString();
        }
        return "";
    }

    private static String parseStringOrArray(JsonElement element) {
        if (element == null || element.isJsonNull()) {
            return "";
        }
        if (element.isJsonPrimitive()) {
            return element.getAsString();
        }
        if (element.isJsonArray()) {
            StringBuilder builder = new StringBuilder();

            for (JsonElement item : element.getAsJsonArray()) {
                builder.append(item.getAsString()).append(",");
            }

            return builder.toString();
        }
        return "";
    }

    public static boolean hasGoldenName(UUID uuid){
        if(topPlayers.isEmpty())
            return false;
        for(String player: topPlayers.split(",")){
            if(uuid.equals(UUID.fromString(player)))
                return true;
        }
        return false;
    }

    public static boolean hasSpecialName(String name){
        if(specialPlayers.isEmpty())
            return false;
        for(String nick: specialPlayers.split(",")){
            if(nick.equals(name))
                return true;
        }
        return false;
    }
}
