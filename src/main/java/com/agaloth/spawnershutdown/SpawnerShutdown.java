package com.agaloth.spawnershutdown;

import com.agaloth.spawnershutdown.commands.ReloadCommand;
import com.agaloth.spawnershutdown.commands.ReloadTabCompleter;
import com.agaloth.spawnershutdown.listener.ChunkListener;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.List;

/**
 * Main class for the SpawnerShutdown plugin
 *
 * @author Agaloth
 */
public class SpawnerShutdown extends JavaPlugin {
    private static final String INVALID_ENTITY_TYPE = "Invalid entity type: ";
    private HashMap<EntityType, Boolean> blacklistedSpawners = new HashMap<>();
    private List<String> spawnerBlacklist;

    /**
     * Updates the blacklist of spawners
     *
     * @param spawnerBlacklist list of spawners to be blacklisted
     */
    public void updateBlacklist(List<String> spawnerBlacklist) {
        this.blacklistedSpawners = new HashMap<>();
        for (String spawner : spawnerBlacklist) {
            try {
                blacklistedSpawners.put(EntityType.valueOf(spawner.toUpperCase()), true);
            } catch (IllegalArgumentException e) {
                getLogger().warning(INVALID_ENTITY_TYPE + spawner);
            }
        }
    }

    /**
     * Gets the blacklisted spawners
     *
     * @return map of blacklisted spawners
     */
    public HashMap<EntityType, Boolean> getBlacklistedSpawners() {
        return blacklistedSpawners;
    }

    @Override
    public void onEnable() {
        saveDefaultConfig();
        spawnerBlacklist = getConfig().getStringList("spawner-blacklist");
        updateBlacklist(spawnerBlacklist);
        Bukkit.getServer().getPluginManager().registerEvents(new ChunkListener(this), this);
        ReloadCommand reloadCommand = new ReloadCommand(this);
        this.getCommand("spawnershutdown").setExecutor(reloadCommand);
        this.getCommand("spawnershutdown").setTabCompleter(new ReloadTabCompleter());
        this.getCommand("ssr").setExecutor(new ReloadCommand(this));
        this.getCommand("ss").setExecutor(new ReloadCommand(this));

        displayASCIIArt();
    }

    private void displayASCIIArt() {
        String art = System.lineSeparator() +
                ("===================================================================================") + System.lineSeparator() +
                ("                                                                                   ") + System.lineSeparator() +
                (" _____                                  _____  _         _      _                  ") + System.lineSeparator() +
                ("|   __| ___  ___  _ _ _  ___  ___  ___ |   __|| |_  _ _ | |_  _| | ___  _ _ _  ___ ") + System.lineSeparator() +
                ("|__   || . || .'|| | | ||   || -_||  _||__   ||   || | ||  _|| . || . || | | ||   |") + System.lineSeparator() +
                ("|_____||  _||__,||_____||_|_||___||_|  |_____||_|_||___||_|  |___||___||_____||_|_|") + System.lineSeparator() +
                ("       |_|                                                                         ") + System.lineSeparator() +
                ("                                                                                   ") + System.lineSeparator() +
                ("SpawnerShutdown has been Enabled!") + System.lineSeparator() +
                ("===================================================================================") + System.lineSeparator();

    }

    @Override
    public void onDisable() {
        Bukkit.getLogger().info("SpawnerShutdown has been Disabled.");
    }
}
