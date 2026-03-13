package com.noaisu.sleepvote;

import org.bukkit.util.config.Configuration;
import org.jetbrains.annotations.Nullable;

import java.io.File;

/**
 * A custom configuration class for managing plugin configuration files in a
 * Bukkit environment.
 * Extends the {@link Configuration} class to provide additional utility methods
 * for
 * reading and writing configuration options with defaults.
 */
public class Config extends Configuration {
    private final int configVersion = 1;

    private Plugin plugin;
    public String sleepPercentageKey = "settings.sleepPercentage.value";
    public String sleepPercentageCommentKey = "settings.sleepPercentage.comment";
    public String sleepNumberKey = "settings.sleepNumber.value";
    public String sleepNumberCommentKey = "settings.sleepNumber.comment";
    public String rainChanceKey = "settings.rainChance.value";
    public String rainChanceCommentKey = "settings.rainChance.comment";
    public String rainDaysKey = "settings.rainDays.value";
    public String rainDaysCommentKey = "settings.rainDays.comment";
    public String thunderChanceKey = "settings.thunderChance.value";
    public String thunderChanceCommentKey = "settings.thunderChance.comment";
    public String clearDaysKey = "settings.clearDays.value";
    public String clearDaysCommentKey = "settings.clearDays.comment";

    /**
     * Constructs a new TemplateConfig instance.
     *
     * @param plugin     The plugin instance associated with this configuration.
     * @param configFile The configuration file to be managed.
     */
    public Config(Plugin plugin, File configFile) {
        super(configFile);
        this.plugin = plugin;
        this.reload();
    }

    /**
     * Writes default configuration options to the file.
     * Ensures that default options are added when the file is loaded.
     */
    private void write() {
        // Convert old configuration keys to new keys if necessary
        if (this.getString("config-version") == null
                || Integer.valueOf(this.getString("config-version")) < configVersion) {
            this.plugin.logger(java.util.logging.Level.INFO,
                    "Converting config to new version (" + configVersion + ")...");
            convertToNewConfig();
            this.setProperty("config-version", configVersion); // This should be handled by the conversion method but
                                                               // just in case
        }

        // Main options
        generateConfigOption("config-version", configVersion);

        // Plugin options
        generateConfigOption(sleepPercentageKey, 0.5);
        generateConfigOption(sleepPercentageCommentKey,
                "Percentage of sleeping players needed to change to day");
        generateConfigOption(sleepNumberKey, 0);
        generateConfigOption(sleepNumberCommentKey,
                "Amount of sleeping players needed to change to day, will override percentage if set higher than 0");
        generateConfigOption(rainChanceKey, 0.3);
        generateConfigOption(rainChanceCommentKey,
                "Percentage chance of rain after sleeping");
        generateConfigOption(rainDaysKey, 3);
        generateConfigOption(rainDaysCommentKey,
                "Max length of rain double as ingame days");
        generateConfigOption(thunderChanceKey, 0.4);
        generateConfigOption(thunderChanceCommentKey,
                "Percentage chance of a thunderstorm if raining after sleeping");
        generateConfigOption(clearDaysKey, 7.5);
        generateConfigOption(clearDaysCommentKey,
                "Max length of clear weather as ingame days");

    }

    private void convertToNewConfig() {
        // Convert old configuration keys to new keys

        // Convert from old config version 0 to new config version 1
        /*
         * if (this.getString("config-version") == null ||
         * Integer.valueOf(this.getString("config-version")) < 1) {
         * convertToNewAddress("settings.test-command-response.value",
         * "settings.test-command.response.value", true);
         * convertToNewAddress("settings.test-command.enabled",
         * "settings.test-command.enabled.value", true);
         * }
         */
    }

    /**
     * Reloads the configuration by loading the file, writing defaults, and saving
     * changes.
     */
    private void reload() {
        this.load();
        this.write();
        this.save();
    }

    /**
     * Converts an old configuration key to a new one.
     * If the old key exists and the new key does not, the old key's value is copied
     * to the new key,
     * and the old key is removed.
     *
     * @param newKey The new configuration key.
     * @param oldKey The old configuration key.
     * @param log    Whether to log the conversion process.
     * @return True if the conversion was performed, false otherwise.
     */
    private boolean convertToNewAddress(String newKey, String oldKey, boolean log) {
        if (this.getString(newKey) != null) {
            return false;
        }
        if (this.getString(oldKey) == null) {
            return false;
        }
        if (log) {
            plugin.logger(java.util.logging.Level.INFO, "Converting Config: " + oldKey + " to " + newKey);
        }
        Object value = this.getProperty(oldKey);
        this.setProperty(newKey, value);
        this.removeProperty(oldKey);
        return true;
    }

    /**
     * Adds a default value for a configuration key if it is not already set.
     *
     * @param key          The configuration key.
     * @param defaultValue The default value to set.
     */
    public void generateConfigOption(String key, Object defaultValue) {
        if (this.getProperty(key) == null) {
            this.setProperty(key, defaultValue);
        }
        final Object value = this.getProperty(key);
        this.removeProperty(key);
        this.setProperty(key, value);
    }

    // Getters Start
    public Object getConfigOption(String key) {
        return this.getProperty(key);
    }

    public String getConfigString(String key) {
        return String.valueOf(getConfigOption(key));
    }

    public Integer getConfigInteger(String key) {
        return Integer.valueOf(getConfigString(key));
    }

    public Long getConfigLong(String key) {
        return Long.valueOf(getConfigString(key));
    }

    public Double getConfigDouble(String key) {
        return Double.valueOf(getConfigString(key));
    }

    public Boolean getConfigBoolean(String key) {
        return Boolean.valueOf(getConfigString(key));
    }

    // Getters End
}