package net.trueog.celeste;

import net.trueog.celeste.commands.CommandCeleste;
import net.trueog.celeste.commands.CommandFallingStar;
import net.trueog.celeste.commands.CommandShootingStar;
import net.trueog.celeste.config.CelesteConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class Celeste extends JavaPlugin {

    public CelesteConfigManager configManager = new CelesteConfigManager(this);

    @Override
    public void onEnable() {
        this.saveDefaultConfig();

        this.getCommand("celeste").setExecutor(new CommandCeleste(this));
        this.getCommand("shootingstar").setExecutor(new CommandShootingStar(this));
        this.getCommand("fallingstar").setExecutor(new CommandFallingStar(this));
        configManager.processConfigs();

        BukkitRunnable stargazingTask = new Astronomer(this);
        stargazingTask.runTaskTimer(this, 0, 10);
    }

    public void reload() {
        reloadConfig();
        configManager.processConfigs();
    }

    public static void getLogger(String message) {

        Bukkit.getLogger().info("[Celeste-OG]" + message);
    }
}
