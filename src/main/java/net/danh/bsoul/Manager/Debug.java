package net.danh.bsoul.Manager;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.danh.bsoul.bSoul;
import net.danh.dcore.DCore;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Debug {

    public static void checkGitUpdate() {
        Logger logger = bSoul.getInstance().getLogger();
        logger.log(Level.INFO, "Github latest build: " + getGitBuild());
        logger.log(Level.INFO, "Github latest changelog: ");
        getGitMessage().forEach(message -> {
            logger.log(Level.INFO, message);
        });
    }


    public static @NotNull String getGitBuild() {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(
                    "https://api.github.com/repos/D-x-Z/bSoul/commits");
            CloseableHttpResponse response = client.execute(request);
            String responseBody = EntityUtils.toString(response.getEntity());

            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(responseBody);
            JsonNode latestCommit = root.get(0);
            String sha = latestCommit.get("sha").textValue();
            return sha.substring(0, Math.min(7, sha.length()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static @NotNull List<String> getGitMessage() {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(
                    "https://api.github.com/repos/D-x-Z/bSoul/commits");
            CloseableHttpResponse response = client.execute(request);
            String responseBody = EntityUtils.toString(response.getEntity());

            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(responseBody);
            JsonNode latestCommit = root.get(0);
            String sha = latestCommit.get("commit").get("message").textValue();
            return new ArrayList<>(Arrays.asList(sha.split("\\n")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void update27() {
        List<Integer> update = Resources.getconfigfile().getIntegerList("SETTINGS.BLACKLIST_SLOTS");
        if (!Resources.getconfigfile().contains("SETTINGS.BLACKLIST_SLOTS") || update.isEmpty()) {
            bSoul.getInstance().getLogger().log(Level.WARNING, "You need add BLACKLIST_SLOTS below SETTINGS (config.yml) to get update 2.7");
            bSoul.getInstance().getLogger().log(Level.WARNING, "Open jar with winrar to check full config!");
        }
    }

    public static void update29() {
        if (!Resources.getconfigfile().contains("DEATH.DROP_ITEM")) {
            Resources.getconfigfile().set("DEATH.DROP_ITEM", true);
            Resources.saveconfig();
            bSoul.getInstance().getLogger().log(Level.WARNING, "Config has been updated for version 2.9!");
        }
    }

    public static void debug(String message) {
        if (Resources.getconfigfile().getBoolean("SETTINGS.DEBUG")) {
            DCore.dCoreLog(message);
        }
    }
}
