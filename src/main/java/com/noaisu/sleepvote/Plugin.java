package com.noaisu.sleepvote;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerBedEnterEvent;

import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.File;

public class Plugin extends JavaPlugin implements Listener {
    private JavaPlugin plugin;
    private Logger log;
    private String pluginName;
    private PluginDescriptionFile pdf;
    private Config config;

    @Override
    public void onEnable() {
        plugin = this;
        log = this.getServer().getLogger();
        pdf = this.getDescription();
        pluginName = pdf.getName();

        log.info("[" + pluginName + "] Is Loading, Version: " + pdf.getVersion());

        config = new Config(this, new File(getDataFolder(), "config.yml"));

        getServer().getPluginManager().registerEvents(this, this);

        log.info("[" + pluginName + "] Is Loaded, Version: " + pdf.getVersion());
    }

    @Override
    public void onDisable() {
        log.info("[" + pluginName + "] Is Unloading, Version: " + pdf.getVersion());
        log.info("[" + pluginName + "] Is Unloaded, Version: " + pdf.getVersion());
    }

    public void logger(Level level, String message) {
        Bukkit.getLogger().log(level, "[" + plugin.getDescription().getName() + "] " + message);
    }

    @EventHandler
    public void onBedEnter(PlayerBedEnterEvent event) {
        World world = event.getPlayer().getWorld();
        int players = world.getPlayers().size();
        int sleepers = 0;

        double percentage = config.getConfigDouble(config.sleepPercentageKey);
        int sleepNumber = config.getConfigInteger(config.sleepNumberKey);

        if (sleepNumber == 0) {
            sleepNumber = (int) Math.floor(players * percentage);
        }

        for (Player p : world.getPlayers()) {
            if (p.isSleeping() || p.equals(event.getPlayer())) {
                sleepers++;
            }
        }

        Bukkit.broadcastMessage(event.getPlayer().getName() + " is eepy (" + sleepers + "/" + sleepNumber + ")");

        if (sleepers >= sleepNumber) {
            world.setTime(0);
            Bukkit.broadcastMessage("Player kun... ohayou (^v^)");
        }
    }
}
