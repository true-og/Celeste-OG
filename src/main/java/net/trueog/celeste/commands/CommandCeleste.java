package net.trueog.celeste.commands;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import net.trueog.celeste.Celeste;

public class CommandCeleste implements CommandExecutor {

    Celeste celeste;

    public CommandCeleste(Celeste celeste) {

        this.celeste = celeste;

    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args.length == 1 && StringUtils.equalsIgnoreCase(args[0], "reload")) {

            if (sender.hasPermission("celeste.reload")) {

                celeste.reload();
                sender.sendMessage("Celeste has been reloaded");

            } else {

                sender.sendMessage("You do not have permission to use this command");

            }

            return true;

        } else if (args.length == 1 && StringUtils.equalsIgnoreCase(args[0], "info")) {

            if (sender.hasPermission("celeste.info")) {

                double fallingStarRate = 0;
                final double adaptiveFallingStars = celeste.getConfig().getDouble("adaptive-falling-stars");
                if (adaptiveFallingStars != 0) {

                    if (celeste.getConfig().getBoolean("adaptive-use-global-player-count")) {

                        fallingStarRate = (celeste.getServer().getOnlinePlayers().size() / adaptiveFallingStars) * 0.1;

                    } else {

                        final List<World> worlds = celeste.getServer().getWorlds();
                        double highestRate = 0;
                        for (World world : worlds) {

                            final double worldRate = (world.getPlayers().size() / adaptiveFallingStars) * 0.1;
                            if (highestRate < worldRate) {

                                highestRate = worldRate;

                            }

                        }

                        fallingStarRate = highestRate;

                    }

                }

                fallingStarRate = fallingStarRate + celeste.getConfig().getDouble("falling-stars-per-minute");

                sender.sendMessage("Celeste-OG v" + celeste.getPluginMeta().getVersion());
                sender.sendMessage("Shooting stars: "
                        + (celeste.getConfig().getBoolean("shooting-stars-enabled") ? "Enabled" : "Disabled"));
                sender.sendMessage("Falling stars: "
                        + (celeste.getConfig().getBoolean("falling-stars-enabled") ? "Enabled" : "Disabled"));
                sender.sendMessage("Falling stars rate (highest): " + fallingStarRate);
                sender.sendMessage("Meteor showers: "
                        + (celeste.getConfig().getBoolean("new-moon-meteor-shower") ? "Enabled" : "Disabled"));

            } else {

                sender.sendMessage("You do not have permission to use this command");

            }

            return true;

        }

        return false;

    }

}