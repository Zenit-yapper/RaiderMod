package com.voracious.hub.api;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TierAPI {
    private static final HttpClient CLIENT = HttpClient.newBuilder()
            .followRedirects(HttpClient.Redirect.ALWAYS)
            .build();
    private static final ConcurrentHashMap<String, String> RANK_CACHE = new ConcurrentHashMap<>();
    private static final Pattern RANK_PATTERN = Pattern.compile("\"(?:rank|tier)\"\\s*:\\s*\"([^\"]+)\"");
    private static final String API_URL = "https://voracious-raider-rank-hub.base44.app/api/player/%s";

    public static String getRank(String username) {
        return RANK_CACHE.getOrDefault(username, null);
    }

    public static void fetch(String username) {
        if (RANK_CACHE.containsKey(username) || username == null) return;

        // Set to loading to prevent duplicate requests
        RANK_CACHE.put(username, "...");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(String.format(API_URL, username)))
                .header("Accept", "application/json")
                .GET()
                .build();

        CLIENT.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(body -> {
                    Matcher matcher = RANK_PATTERN.matcher(body);
                    if (matcher.find()) {
                        RANK_CACHE.put(username, matcher.group(1));
                    } else {
                        // If not found, we could put a default or remove from cache to retry later
                        RANK_CACHE.remove(username);
                    }
                })
                .exceptionally(ex -> {
                    RANK_CACHE.remove(username);
                    return null;
                });
    }

    public static void clearCache() {
        RANK_CACHE.clear();
    }
}
