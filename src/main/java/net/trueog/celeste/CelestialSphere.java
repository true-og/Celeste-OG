package net.trueog.celeste;

import java.math.RoundingMode;
import java.security.SecureRandom;
import java.text.DecimalFormat;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import net.trueog.celeste.config.CelesteConfig;

public class CelestialSphere {

    public static void createShootingStar(Celeste celeste, Player player) {

        createShootingStar(celeste, player, true);

    }

    public static void createShootingStar(Celeste celeste, Player player, boolean approximate) {

        createShootingStar(celeste, player.getLocation(), approximate);

    }

    public static void createShootingStar(Celeste celeste, Location location) {

        createShootingStar(celeste, location, true);

    }

    public static void createShootingStar(Celeste celeste, Location location, boolean approximate) {

        final Location starLocation;
        final CelesteConfig config = celeste.configManager.getConfigForWorld(location.getWorld().getName());
        final double w = 100 * Math.sqrt(new SecureRandom().nextDouble());
        final double t = 2d * Math.PI * new SecureRandom().nextDouble();
        final double x = w * Math.cos(t);
        final double range = Math.max(0, config.shootingStarsMaxHeight - config.shootingStarsMinHeight);
        final double y = Math.max(new SecureRandom().nextDouble() * range + config.shootingStarsMinHeight,
                location.getY() + 50);
        final double z = w * Math.sin(t);
        if (approximate) {

            starLocation = new Location(location.getWorld(), location.getX() + x, y, location.getZ() + z);

        } else {

            starLocation = new Location(location.getWorld(), location.getX(), y, location.getZ());

        }

        final Vector direction = new Vector(new Random().nextDouble() * 2 - 1, new Random().nextDouble() * -0.75d,
                new Random().nextDouble() * 2 - 1);
        direction.normalize();
        final double speed = new SecureRandom().nextDouble() * 2 + 0.75;
        location.getWorld().spawnParticle(Particle.FIREWORKS_SPARK, starLocation, 0, direction.getX(), direction.getY(),
                direction.getZ(), speed, null, true);
        if (new SecureRandom().nextDouble() >= 0.5) {

            location.getWorld().spawnParticle(Particle.EXPLOSION_NORMAL, starLocation, 0, direction.getX(),
                    direction.getY(), direction.getZ(), speed, null, true);

        }

        if (celeste.getConfig().getBoolean("debug")) {

            celeste.getLogger().info("Shooting star at " + stringifyLocation(starLocation) + " in world "
                    + starLocation.getWorld().getName());

        }

    }

    public static void createFallingStar(Celeste celeste, Player player) {

        createFallingStar(celeste, player, true);

    }

    public static void createFallingStar(Celeste celeste, Player player, boolean approximate) {

        createFallingStar(celeste, player.getLocation(), approximate);

    }

    public static void createFallingStar(Celeste celeste, final Location location) {

        createFallingStar(celeste, location, true);

    }

    public static void createFallingStar(Celeste celeste, final Location location, boolean approximate) {

        Location target = location;
        final CelesteConfig config = celeste.configManager.getConfigForWorld(location.getWorld().getName());
        if (approximate) {

            final double fallingStarRadius = config.fallingStarsRadius;
            final double w = fallingStarRadius * Math.sqrt(new SecureRandom().nextDouble());
            final double t = 2d * Math.PI * new SecureRandom().nextDouble();
            final double x = w * Math.cos(t);
            final double z = w * Math.sin(t);

            target = new Location(location.getWorld(), location.getX() + x, location.getY(), location.getZ() + z);

        }

        final BukkitRunnable fallingStarTask = new FallingStar(celeste, target);
        fallingStarTask.runTaskTimer(celeste, 0, 1);
        if (celeste.getConfig().getBoolean("debug")) {

            celeste.getLogger()
                    .info("Falling star at " + stringifyLocation(target) + " in world " + target.getWorld().getName());

        }

    }

    private static String stringifyLocation(Location location) {

        final DecimalFormat format = new DecimalFormat("##.00");

        format.setRoundingMode(RoundingMode.HALF_UP);

        return "x: " + format.format(location.getX()) + " y: " + format.format(location.getY()) + " z: "
                + format.format(location.getZ());

    }

}