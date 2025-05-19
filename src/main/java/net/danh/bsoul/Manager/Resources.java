package net.danh.bsoul.Manager;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

import static net.danh.bsoul.bSoul.getInstance;

public class Resources {

    private static File configFile, languageFile, mobFile;
    private static FileConfiguration config, language, mob;

    public static void createfiles() {
        configFile = new File(getInstance().getDataFolder(), "config.yml");
        languageFile = new File(getInstance().getDataFolder(), "language.yml");
        mobFile = new File(getInstance().getDataFolder(), "mob.yml");

        if (!configFile.exists()) getInstance().saveResource("config.yml", false);
        if (!languageFile.exists()) getInstance().saveResource("language.yml", false);
        if (!mobFile.exists()) getInstance().saveResource("mob.yml", false);
        language = new YamlConfiguration();
        config = new YamlConfiguration();
        mob = new YamlConfiguration();

        try {
            language.load(languageFile);
            config.load(configFile);
            mob.load(mobFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
        FileLoader fileLoader = new FileLoader();
        fileLoader.load();
    }

    public static FileConfiguration getconfigfile() {
        return config;
    }

    public static FileConfiguration getlanguagefile() {
        return language;
    }

    public static FileConfiguration getmobfile() {
        return mob;
    }

    public static void reloadfiles() {
        language = YamlConfiguration.loadConfiguration(languageFile);
        config = YamlConfiguration.loadConfiguration(configFile);
        mob = YamlConfiguration.loadConfiguration(mobFile);
        FileLoader fileLoader = new FileLoader();
        fileLoader.load();
    }

    public static void saveconfig() {
        try {
            config.save(configFile);
        } catch (IOException ignored) {
        }
        FileLoader fileLoader = new FileLoader();
        fileLoader.load();
    }

    public static void savelanguage() {
        try {
            language.save(languageFile);
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
