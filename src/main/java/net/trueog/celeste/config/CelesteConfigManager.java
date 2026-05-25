package net.trueog.celeste.config;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import net.trueog.celeste.Celeste;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

public class CelesteConfigManager {

    private final Celeste celeste;
    private final Map<String, CelesteConfig> worldConfigs = new HashMap<>();
    private final Set<String> fallingStarsWorldWhitelist = new HashSet<>();
    private CelesteConfig globalConfig;

    public CelesteConfigManager(Celeste celeste) {

        this.celeste = celeste;

    }

    public void processConfigs() {

        this.worldConfigs.clear();
        this.fallingStarsWorldWhitelist.clear();

        final FileConfiguration config = this.celeste.getConfig();
        globalConfig = new CelesteConfig(config);

        this.fallingStarsWorldWhitelist.addAll(config.getStringList("falling-stars-world-whitelist"));

        final ConfigurationSection worlds = config.getConfigurationSection("world-overrides");
        if (worlds != null) {

            for (String world : worlds.getKeys(false)) {

                final ConfigurationSection worldSettings = worlds.getConfigurationSection(world);
                if (worldSettings == null) {

                    this.celeste.getLogger().severe("Your world override config for world '" + world
                            + "' is malformed, please review example configs at https://github.com/IdreesInc/Celeste");
                    continue;

                }

                final CelesteConfig worldConfig = new CelesteConfig(worldSettings, globalConfig);
                worldConfigs.put(world, worldConfig);

            }

        }

    }

    /**
     * Get the config for the given world name (case-sensitive) or the global config
     * if no overrides have been added
     * 
     * @param worldName The name of the world
     * @return The config that applies to the given world
     */
    public CelesteConfig getConfigForWorld(String worldName) {

        CelesteConfig config = worldConfigs.get(worldName);
        if (config == null) {

            config = globalConfig;

        }

        return config;

    }

    /**
     * Check whether the world has been configured with specific overrides in the
     * config file
     * 
     * @return Whether the specified world's configuration has been modified
     */
    public boolean doesWorldHaveOverrides(String worldName) {

        return worldConfigs.containsKey(worldName);

    }

    /**
     * Check whether falling stars are allowed to spawn in the given world. If the
     * whitelist is empty it is treated as disabled and all worlds are allowed,
     * otherwise only worlds listed in falling-stars-world-whitelist are allowed.
     *
     * @param worldName The name of the world (case-sensitive)
     * @return Whether falling stars may spawn in the given world
     */
    public boolean isFallingStarsAllowedInWorld(String worldName) {

        return fallingStarsWorldWhitelist.isEmpty() || fallingStarsWorldWhitelist.contains(worldName);

    }

}