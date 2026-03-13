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
    int ticksPerDay = 24000;

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

        if (sleepNumber == 0) {
            sleepNumber = 1;
        }

        for (Player p : world.getPlayers()) {
            if (p.isSleeping() || p.equals(event.getPlayer())) {
                sleepers++;
            }
        }

        String[] playerSuffixes = { "san", "chan", "kun", "sama", "senpai", "tan" };

        String playerSuffix = playerSuffixes[(int) (Math.random() * playerSuffixes.length)];

        Bukkit.broadcastMessage(
                event.getPlayer().getName() + "-" + playerSuffix + " is eepy (" + sleepers + "/" + sleepNumber + ")");

        if (sleepers < sleepNumber) {
            return;
        }

        String morningMessage = (players == 1) ? "Player-kun... ohayou (^v^)" : "Mina-san... ohayou (^v^)";

        double rainChance = config.getConfigDouble(config.rainChanceKey);
        double thunderChance = config.getConfigDouble(config.thunderChanceKey);
        double maxRainDays = config.getConfigDouble(config.rainDaysKey);
        double maxClearDays = config.getConfigDouble(config.clearDaysKey);

        String weatherMessage;

        double weather = Math.random();
        int weatherDuration;

        if (weather <= rainChance) {

            world.setStorm(true);
            weatherDuration = getRandomTicks(maxRainDays * ticksPerDay, 0.5 * ticksPerDay);

            double thunder = Math.random();

            if (thunder <= thunderChance) {
                world.setThundering(true);
                weatherMessage = "Kyo wa arashi desu (O_O)";
                int thunderDays = (int) getRandomTicks(weatherDuration, 3600);
                world.setThunderDuration(thunderDays);
            } else {
                world.setThundering(false);
                weatherMessage = "Kyo wa ame desu (>_<)";

            }
        } else {
            world.setStorm(false);
            world.setThundering(false);
            weatherDuration = getRandomTicks(maxClearDays * ticksPerDay, 0.5 * ticksPerDay);
            weatherMessage = "Kyo wa ii tenki desu \\(^o^)/";
        }
        world.setWeatherDuration(weatherDuration);
        world.setTime(0);
        Bukkit.broadcastMessage(morningMessage);
        Bukkit.broadcastMessage(weatherMessage);
    }

    int getRandomTicks(double maxTicks, double minTicks) {
        return (int) (Math.random() * (maxTicks - minTicks) + minTicks);
    }
}
