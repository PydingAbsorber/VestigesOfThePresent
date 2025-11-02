package com.pyding.vp.util;

import com.pyding.vp.VestigesOfThePresent;
import com.pyding.vp.capability.PlayerCapabilityProviderVP;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class LeaderboardUtil {
    public static String topPlayers = "";
    public static String specialPlayers = "";
    public static boolean exception = false;

    public static String getHost() {
        if(ConfigHandler.COMMON_SPEC.isLoaded() && !ConfigHandler.COMMON.leaderboardHost.get().toString().isEmpty())
            return ConfigHandler.COMMON.leaderboardHost.get().toString();
        return "www.pyding.org";
    }

    public static String addNickname(Player player, UUID uuid, String password){
        if(!isLeaderboardsActive(player) || isCheating(player)){
            return "Leaderboards disabled.";
        }
        AtomicReference<String> message = new AtomicReference<>("");
        CompletableFuture.runAsync(() -> {
            try {
                HttpClient client = HttpClient.newBuilder()
                        .version(HttpClient.Version.HTTP_2)
                        .build();

                String url = "https://" + getHost() +
                        "/addNickname?nickName=" + URLEncoder.encode(player.getScoreboardName(), StandardCharsets.UTF_8) +
                        "&UUID=" + uuid +
                        "&version=" + URLEncoder.encode(getCurrentVersion(), StandardCharsets.UTF_8) +
                        "&password=" + URLEncoder.encode(password, StandardCharsets.UTF_8);

                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(url))
                        .GET()
                        .build();

                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                message.set(response.body());
            } catch (Exception e) {
                player.sendSystemMessage(Component.literal(e.getMessage()).withStyle(ChatFormatting.DARK_RED));
            }
        });
        return message.get();
    }

    public static void checkPassword(Player player, UUID uuid, String password){
        if(!isLeaderboardsActive(player) || isCheating(player)){
            player.sendSystemMessage(Component.literal("Leaderboard is disabled or you are cheating").withStyle(ChatFormatting.RED));
            return;
        }
        CompletableFuture.runAsync(() -> {
            try {
                HttpClient client = HttpClient.newBuilder()
                        .version(HttpClient.Version.HTTP_2)
                        .build();
                String url = "https://" + getHost() +
                        "/login?nickName=" + URLEncoder.encode(player.getScoreboardName(), StandardCharsets.UTF_8) +
                        "&UUID=" + URLEncoder.encode(uuid.toString(), StandardCharsets.UTF_8) +
                        "&version=" + URLEncoder.encode(getCurrentVersion(), StandardCharsets.UTF_8) +
                        "&password=" + URLEncoder.encode(password, StandardCharsets.UTF_8);
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(url))
                        .GET()
                        .build();
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                if(response.body().equals("ok")) {
                        player.sendSystemMessage(Component.literal("You logged in successfully.").withStyle(ChatFormatting.GREEN));
                        player.getCapability(PlayerCapabilityProviderVP.playerCap).ifPresent(cap -> {
                            cap.setPassword(password);
                            cap.sync(player);
                        });
                } else player.sendSystemMessage(Component.literal(response.body()).withStyle(ChatFormatting.RED));
            } catch (Exception e) {
                exception = true;
                player.sendSystemMessage(Component.literal(e.getMessage()).withStyle(ChatFormatting.RED));
            }
        });
    }

    public static void addChallenge(Player player, int id, String password){
        if(!isLeaderboardsActive(player) || isCheating(player)){
            return;
        }
        CompletableFuture.runAsync(() -> {
            try {
                HttpClient client = HttpClient.newBuilder()
                        .version(HttpClient.Version.HTTP_2)
                        .build();
                String url = "https://" + getHost() +
                        "/addChallenge?UUID=" + URLEncoder.encode(player.getUUID().toString(), StandardCharsets.UTF_8) +
                        "&id=" + id +
                        "&version=" + URLEncoder.encode(getCurrentVersion(), StandardCharsets.UTF_8) +
                        "&password=" + URLEncoder.encode(password, StandardCharsets.UTF_8);
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(url))
                        .GET()
                        .build();
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            } catch (Exception e) {
                exception = true;
                e.printStackTrace();
            }
        });
    }

    public static void setCheating(Player player){
        player.getCapability(PlayerCapabilityProviderVP.playerCap).ifPresent(cap -> {
            if(!cap.isCheating()) {
                cap.setCheating(true);
                cap.sync(player);
            }
        });
    }

    public static boolean isCheating(Player player){
        AtomicBoolean atomicBoolean = new AtomicBoolean();
        player.getCapability(PlayerCapabilityProviderVP.playerCap).ifPresent(cap -> {
            atomicBoolean.set(cap.isCheating());
        });
        return atomicBoolean.get();
    }

    public static String getInformation(UUID uuid){
        try {
            HttpClient client = HttpClient.newBuilder()
                    .version(HttpClient.Version.HTTP_2)
                    .build();
            String url = "https://" + getHost() +
                    "/getInformation?UUID=" + URLEncoder.encode(uuid.toString(), StandardCharsets.UTF_8) +
                    "&version=" + URLEncoder.encode(getCurrentVersion(), StandardCharsets.UTF_8);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            return response.body();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "null";
    }

    public static String getAll(){
        try {
            HttpClient client = HttpClient.newBuilder()
                    .version(HttpClient.Version.HTTP_2)
                    .build();
            String url = "https://" + getHost() +
                    "/getAll?version=" + URLEncoder.encode(getCurrentVersion(), StandardCharsets.UTF_8);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.body();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "null";
    }

    public static String check(){
        try {
            HttpClient client = HttpClient.newBuilder()
                    .version(HttpClient.Version.HTTP_2)
                    .build();
            String url = "https://" + getHost() +
                    "/check?version=" + URLEncoder.encode(getCurrentVersion(), StandardCharsets.UTF_8);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.body();
        } catch (Exception e) {
            return "Something is not ok :(((( Backend may be offline \n"+e.getMessage();
        }
    }

    public static void printCheck(Player player){
        player.sendSystemMessage(Component.literal("This may take some time to check"));
        CompletableFuture.runAsync(() -> {
            String response = "";
            try {
                response += check();
            } catch (Exception e){
                response += e.getMessage();
            }
            player.sendSystemMessage(Component.literal(response));
        });
    }

    public static void printAll(Player player){
        CompletableFuture.runAsync(() -> {
            String response = VPUtil.filterString(getAll());
            player.sendSystemMessage(Component.literal("Position | Nickname | Challenges | Unique Challenges"));
            int count = 0;
            for(String name: response.split(",")){
                if(count < 10) {
                    count++;
                    player.sendSystemMessage(GradientUtil.goldenGradient(name.replaceAll("^\"|\"$", "")));
                } else player.sendSystemMessage(Component.literal(name.replaceAll("^\"|\"$", "")));
            }
        });
    }

    public static void printYourself(Player player){
        CompletableFuture.runAsync(() -> player.sendSystemMessage(Component.literal(getInformation(player.getUUID()))));
    }

    public static String getTopPlayers(){
        if(exception)
            return "exception";
        if(topPlayers.isEmpty()) {
            CompletableFuture.runAsync(() -> {
                try {
                    HttpClient client = HttpClient.newBuilder()
                            .version(HttpClient.Version.HTTP_2)
                            .build();
                    String url = "https://" + getHost() +
                            "/getTop?version=" + URLEncoder.encode(getCurrentVersion(), StandardCharsets.UTF_8);
                    HttpRequest request = HttpRequest.newBuilder()
                            .uri(URI.create(url))
                            .GET()
                            .build();
                    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                    topPlayers = response.body();
                } catch (Exception e) {
                    exception = true;
                    specialPlayers = "Something is not ok :(((( Backend may be offline \n" + e.getMessage();
                }
            });
        }
        return topPlayers;
    }

    public static String getSpecialPlayers(){
        if(exception)
            return "exception";
        if(specialPlayers.isEmpty()) {
            CompletableFuture.runAsync(() -> {
                try {
                    HttpClient client = HttpClient.newBuilder()
                            .version(HttpClient.Version.HTTP_2)
                            .build();
                    String url = "https://" + getHost() +
                            "/getSpecial?version=" + URLEncoder.encode(getCurrentVersion(), StandardCharsets.UTF_8);
                    HttpRequest request = HttpRequest.newBuilder()
                            .uri(URI.create(url))
                            .GET()
                            .build();
                    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                    specialPlayers = response.body();
                } catch (Exception e) {
                    exception = true;
                    specialPlayers = "Something is not ok :(((( Backend may be offline \n" + e.getMessage();
                }
            });
        }
        return specialPlayers;
    }

    public static void refreshTopPlayers(){
        getTopPlayers();
        getSpecialPlayers();
    }

    public static String getCurrentVersion(){
        return VestigesOfThePresent.VERSION;
    }

    public static void printVersion(Player player){
        CompletableFuture.runAsync(() -> {
            try {
                String request = getVersion();
                if(!request.isEmpty() && !request.equals(getCurrentVersion().split(":")[1]))
                    player.sendSystemMessage(Component.literal("§7From §5Vestiges of the Present mod: §7You are running " + getCurrentVersion().split(":")[1] + " version. Please update to latest " + request + " version."));
            } catch (Exception e){
                System.out.println(e.getMessage());
            }
        });
    }

    public static String getVersion(){
        try {
            String[] currentVersion = VestigesOfThePresent.VERSION.split(":");
            HttpClient client = HttpClient.newBuilder()
                    .version(HttpClient.Version.HTTP_2)
                    .build();
            String url = "https://" + getHost() +
                    "/getVersion?version=" + URLEncoder.encode(currentVersion[0], StandardCharsets.UTF_8);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.body();
        } catch (Exception e) {
            return "";
        }
    }

    public static boolean hasGoldenName(UUID uuid){
        if(exception)
            return false;
        try {
            for(String player: getTopPlayers().split(",")){
                if(uuid.equals(UUID.fromString(player)))
                    return true;
            }
        } catch (Exception e) {
        }
        return false;
    }

    public static boolean hasSpecialName(String name){
        if(exception)
            return false;
        try {
            for(String nick: getSpecialPlayers().split(",")){
                if(nick.equals(name))
                    return true;
            }
        } catch (Exception e) {
        }
        return false;
    }

    public static boolean isLeaderboardsActive(Player player){
        return ConfigHandler.COMMON.leaderboard.get() && !player.hasPermissions(2);
    }
}
