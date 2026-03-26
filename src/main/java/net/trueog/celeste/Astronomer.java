package net.trueog.celeste;

import java.security.SecureRandom;
import java.util.List;

import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;

import net.trueog.celeste.config.CelesteConfig;

public class Astronomer extends BukkitRunnable {

	private final Celeste celeste;

	public Astronomer(Celeste celeste) {

		this.celeste = celeste;

	}

	@Override
	public void run() {

		if (celeste.getServer().getOnlinePlayers().isEmpty()) {

			return;

		}

		final List<World> worlds = celeste.getServer().getWorlds();
		for (World world : worlds) {

			float multiplier = 1;

			final CelesteConfig config = celeste.configManager.getConfigForWorld(world.getName());
			if (!celeste.configManager.doesWorldHaveOverrides(world.getName())
					&& world.getEnvironment() != World.Environment.NORMAL) {

				// Ensure that Celeste-OG only runs on normal worlds unless override is specified
				// in config
				continue;

			}

			if (world.getPlayers().size() == 0) {

				continue;

			}

			if (!(world.getTime() >= config.beginSpawningStarsTime && world.getTime() <= config.endSpawningStarsTime)) {

				if (!config.starsDaylight) {

					continue;

				}

				multiplier = config.dayMultiplier;

			}

			if (world.hasStorm()) {

				continue;

			}

			final double shootingStarChance;
			final double fallingStarChance;
			final double shootingStarsPerMin;
			final double fallingStarPerMin;
			double adaptiveShootingStarsChance = 0;
			double adaptiveFallingStarsChance = 0;
			if (config.adaptiveShootingStars != 0) {

				if (config.adaptiveGlobalPlayerCount) {

					adaptiveShootingStarsChance = (celeste.getServer().getOnlinePlayers().size()
							/ config.adaptiveShootingStars) * 0.1;

				}
				else {

					adaptiveShootingStarsChance = (world.getPlayers().size() / config.adaptiveShootingStars) * 0.1;

				}

			}

			if (config.adaptiveFallingStars != 0) {

				if (config.adaptiveGlobalPlayerCount) {

					adaptiveFallingStarsChance = (celeste.getServer().getOnlinePlayers().size()
							/ config.adaptiveFallingStars) * 0.1;

				} else {

					adaptiveFallingStarsChance = (world.getPlayers().size() / config.adaptiveFallingStars) * 0.1;

				}

			}

			if (config.newMoonMeteorShower && (world.getFullTime() / 24000) % 8 == 4) {

				shootingStarsPerMin = adaptiveShootingStarsChance + config.shootingStarsPerMinuteMeteorShower;
				fallingStarPerMin = (adaptiveFallingStarsChance + config.fallingStarsPerMinuteMeteorShower)
						* multiplier;

			}
			else {

				shootingStarsPerMin = adaptiveShootingStarsChance + config.shootingStarsPerMinute;
				fallingStarPerMin = (adaptiveFallingStarsChance + config.fallingStarsPerMinute) * multiplier;

			}

			shootingStarChance = shootingStarsPerMin / 120d;
			fallingStarChance = fallingStarPerMin / 120d;
			if (config.shootingStarsEnabled && new SecureRandom().nextDouble() <= shootingStarChance) {

				CelestialSphere.createShootingStar(celeste,
						world.getPlayers().get(new SecureRandom().nextInt(world.getPlayers().size())));

			}

			if (config.fallingStarsEnabled && new SecureRandom().nextDouble() <= fallingStarChance) {

				CelestialSphere.createFallingStar(celeste,
						world.getPlayers().get(new SecureRandom().nextInt(world.getPlayers().size())));

			}

		}

	}

}