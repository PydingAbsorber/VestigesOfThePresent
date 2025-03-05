package com.pyding.vp.util;

import com.pyding.vp.VestigesOfThePresent;
import com.pyding.vp.capability.PlayerCapabilityProviderVP;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class LeaderboardUtil {
    public static List<String> topPlayers = new ArrayList<>();

    public static String getHost() {
        if(ConfigHandler.COMMON_SPEC.isLoaded() && !ConfigHandler.COMMON.leaderboardHost.get().toString().isEmpty())
            return ConfigHandler.COMMON.leaderboardHost.get().toString();
        return "95.153.103.118";
    }

    public static String getPort() {
        if(ConfigHandler.COMMON_SPEC.isLoaded() && !ConfigHandler.COMMON.leaderboardPort.get().toString().isEmpty())
            return ConfigHandler.COMMON.leaderboardPort.get().toString();
        return "5242";
    }

    public static String addNickname(Player player, UUID uuid, String password){
        if(!isLeaderboardsActive(player) || isCheating(player)){
            return "Leaderboards disabled.";
        }
        AtomicReference<String> message = new AtomicReference<>("");
        CompletableFuture.runAsync(() -> {
            try {
                HttpClient client = HttpClient.newHttpClient();
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create("http://" + getHost() + ":" + getPort() + "/addNickname?nickName=" + player.getScoreboardName() + "&UUID=" + uuid.toString() + "&version=" + getCurrentVersion() + "&password=" +password))
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
                HttpClient client = HttpClient.newHttpClient();
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create("http://" + getHost() + ":" + getPort() + "/login?nickName=" + player.getScoreboardName() + "&UUID=" + uuid.toString() + "&version=" + getCurrentVersion() + "&password=" +password))
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
                HttpClient client = HttpClient.newHttpClient();
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create("http://" + getHost() + ":" + getPort() + "/addChallenge?UUID=" + player.getUUID().toString() + "&id=" + id + "&version=" + getCurrentVersion() + "&password=" +password))
                        .GET()
                        .build();
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public static void setCheating(Player player){
        player.getCapability(PlayerCapabilityProviderVP.playerCap).ifPresent(cap -> {
            cap.setCheating(true);
            cap.sync(player);
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
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://" + getHost() + ":" + getPort() + "/getInformation?UUID=" + uuid.toString() + "&version=" + getCurrentVersion()))
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
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://" + getHost() + ":" + getPort() + "/getAll?version=" + getCurrentVersion()))
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
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://" + getHost() + ":" + getPort() + "/check?version=" + getCurrentVersion()))
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
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://" + getHost() + ":" + getPort() + "/getTop?version=" + getCurrentVersion()))
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.body();
        } catch (Exception e) {
            return "Something is not ok :(((( Backend may be offline \n"+e.getMessage();
        }
    }

    public static void refreshTopPlayers(){
        CompletableFuture.runAsync(() -> {
            try {
                topPlayers.clear();
                for (String element: VPUtil.filterString(getTopPlayers()).split(",")) {
                    topPlayers.add(element.replaceAll("^\"|\"$", ""));
                }
            } catch (Exception e){
                System.out.println(e.getMessage());
            }
        });
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
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://" + getHost() + ":" + getPort() + "/getVersion?version=" + currentVersion[0]))
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.body();
        } catch (Exception e) {
            return "";
        }
    }

    public static boolean hasGoldenName(UUID uuid){
        try {
            if (!topPlayers.isEmpty()) {
                for (String element : topPlayers) {
                    if (UUID.fromString(element).compareTo(uuid) == 0)
                        return true;
                }
            }
        } catch (Exception e) {
        }
        return false;
    }

    public static boolean isLeaderboardsActive(Player player){
        return ConfigHandler.COMMON.leaderboard.get() && !player.hasPermissions(2);
    }
}
