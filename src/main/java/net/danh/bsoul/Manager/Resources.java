package net.danh.bsoul.Manager;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

import static net.danh.bsoul.bSoul.getInstance;

public class Resources {

    private static File configFile, languageFile, dataFile, mobFile;
    private static FileConfiguration config, language, data, mob;

    public static void createfiles() {
        configFile = new File(getInstance().getDataFolder(), "config.yml");
        languageFile = new File(getInstance().getDataFolder(), "language.yml");
        dataFile = new File(getInstance().getDataFolder(), "data.yml");
        mobFile = new File(getInstance().getDataFolder(), "mob.yml");

        if (!configFile.exists()) getInstance().saveResource("config.yml", false);
        if (!languageFile.exists()) getInstance().saveResource("language.yml", false);
        if (!dataFile.exists()) getInstance().saveResource("data.yml", false);
        if (!mobFile.exists()) getInstance().saveResource("mob.yml", false);
        language = new YamlConfiguration();
        data = new YamlConfiguration();
        config = new YamlConfiguration();
        mob = new YamlConfiguration();

        try {
            language.load(languageFile);
            data.load(dataFile);
            config.load(configFile);
            mob.load(mobFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public static FileConfiguration getconfigfile() {
        return config;
    }

    public static FileConfiguration getlanguagefile() {
        return language;
    }

    public static FileConfiguration getdatafile() {
        return data;
    }

    public static FileConfiguration getmobfile() {
        return mob;
    }

    public static void reloadfiles() {
        language = YamlConfiguration.loadConfiguration(languageFile);
        config = YamlConfiguration.loadConfiguration(configFile);
        mob = YamlConfiguration.loadConfiguration(mobFile);
    }

    public static void saveconfig() {
        try {
            config.save(configFile);
        } catch (IOException ignored) {
        }
    }

    public static void savelanguage() {
        try {
            language.save(languageFile);
        } catch (IOException ignored) {
        }
    }

    public static void savedata() {
        try {
            data.save(dataFile);
        } catch (IOException ignored) {
        }
    }
    public static void savemob() {
        try {
            mob.save(mobFile);
        } catch (IOException ignored) {
        }
    }
}
