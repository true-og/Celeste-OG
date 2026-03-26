# Celeste-OG - Add shooting stars to your Minecraft server
Celeste-OG is a 1.19 fork of [Celeste](https://github.com/IdreesInc/Celeste) maintained for [TrueOG Network](https://true-og.net). It keeps the original Celeste concept and documentation while updating the project for this fork's Gradle build, Purpur 1.19.4 target, and additional gameplay/config features merged from upstream community work.

Inspired by the shooting stars from Animal Crossing and the falling stars from Breath of the Wild, this plugin brings some celestial ambience to your Minecraft world.

## Fork Changes
- Gradle build instead of Maven, by [NotAlexNoyle](https://github.com/NotAlexNoyle)
- Purpur API `1.19.4-R0.1-SNAPSHOT`, by [NotAlexNoyle](https://github.com/NotAlexNoyle)
- Java 17 toolchain, by [NotAlexNoyle](https://github.com/NotAlexNoyle)
- Removed bStats metrics, by [NotAlexNoyle](https://github.com/NotAlexNoyle)
- Removed update checker, by [NotAlexNoyle](https://github.com/NotAlexNoyle)
- Configurable falling star max height, by [SpaceXCheeseWheel](https://github.com/SpaceXCheeseWheel)
- Adaptive shooting star and falling star spawn rates based on player count, by [rm20killer](https://github.com/rm20killer)
- Optional daytime falling stars with a configurable multiplier, by [rm20killer](https://github.com/rm20killer)
- `/celeste info` shows the effective highest falling star rate, by [rm20killer](https://github.com/rm20killer)

<a href="https://discord.gg/6yxE9prcNc" target="_blank">
	<img alt="Discord" src="https://img.shields.io/discord/1398471368403583120?logo=discord&logoColor=fff&label=discord&color=5865F2">
</a>

## Features
- Add shooting stars to the night sky with configurable frequency
- Find rare falling stars that deliver precious materials in a sparkling package
- Watch meteor showers that occur every new moon, increasing the rate of shooting and falling stars
- Scale star activity with server or world player counts
- Allow falling stars during the day at a reduced rate if desired
- Override settings per world
- Use either simple weighted loot or Minecraft loot tables for falling star rewards

![Shooting stars and a falling star in the corner](/images/meteor_shower.gif)

### Shooting stars
Shooting stars occur frequently, but because they spawn around random players in large areas, players usually only notice them when they are actually stargazing. They are cosmetic only and exist to make the sky feel alive. Celeste varies their speed, length, and breakup behavior to better resemble real meteors.

![Falling star](/images/falling_star_close.gif)

### Falling stars
Falling stars are much rarer and are meant to be discovered. They streak toward the ground, play an impact sound, and leave behind a short-lived sparkling landing site so players can hunt them down. When found, they can reward configurable loot and experience.

## In-Depth Functionality
Stars are processed per world. When a star is selected to spawn, the plugin chooses a random player in that world and centers the spawn around that player to avoid wasting events on unloaded chunks.

By default, stars only spawn when these conditions are met:
- A world has at least one player in it
- The world time is between `13000` and `23000`
- The weather is clear
- The world is a normal overworld unless explicitly overridden in config

This fork also supports:
- Adaptive spawn bonuses using either global online player count or per-world player count
- Falling stars during daytime when `falling-stars-daylight` is enabled
- Per-world overrides via `world-overrides`

## Build
This repo no longer uses Maven.

Requirements:
- Java 17

Build with:

```bash
./gradlew build
```

Compile only:

```bash
./gradlew compileJava
```

## Installation
Copy the built jar into your server's `plugins/` folder and start the server once to generate the default config.

## Configuration
If your config was created by an older version, it may be missing newly added settings. Add the missing keys from the defaults below as needed.

### Default Config
```yaml
shooting-stars-enabled: true
falling-stars-enabled: true
begin-spawning-stars-time: 13000
end-spawning-stars-time: 23000
shooting-stars-per-minute: 12
shooting-stars-min-height: 160
shooting-stars-max-height: 310
falling-stars-per-minute: 0.2
falling-stars-max-height: 300
falling-stars-radius: 75
falling-stars-sound-enabled: true
falling-stars-volume: 6
falling-stars-spark-time: 200
falling-stars-experience: 25
falling-stars-loot:
  diamond: 1
  emerald: 0
  fire_charge: 0
new-moon-meteor-shower: true
shooting-stars-per-minute-during-meteor-showers: 36
falling-stars-per-minute-during-meteor-showers: 0.4
shooting-stars-summon-text: "&eMake a wish!"
falling-stars-summon-text: "&eWish granted!"
debug: false
# Adaptive settings
# This settings will increase falling-stars-per-minute and shooting-stars-per-minute depending on player count.
# 0 is disabled, 1 will increase it by 0.1 for every player, 2 will increase it by 0.2 for every 2 players etc
adaptive-falling-stars: 0
adaptive-shooting-stars: 0
adaptive-use-global-player-count: true
# Allow falling stars to spawn during the day with a lower chance.
falling-stars-daylight: false
falling-stars-day-multiplicative: 0
```

### Adaptive Spawn Settings
- `adaptive-falling-stars`: Adds to the falling star rate based on player count
- `adaptive-shooting-stars`: Adds to the shooting star rate based on player count
- `adaptive-use-global-player-count`: If `true`, uses total online players; if `false`, uses players in each world

Example:
- `adaptive-falling-stars: 1` adds `0.1` falling stars per minute per player
- `adaptive-falling-stars: 2` adds `0.1` per 2 players
- `0` disables the adaptive bonus entirely

### Daytime Falling Stars
- `falling-stars-daylight`: Enables falling stars outside the configured night window
- `falling-stars-day-multiplicative`: Multiplies the falling star rate during daytime

Example:
- `falling-stars-daylight: true`
- `falling-stars-day-multiplicative: 0.25`

This keeps daytime falling stars possible while making them much rarer than nighttime events.

### Falling Star Loot
Falling stars can reward experience, simple weighted loot, or a Minecraft loot table.

#### Simple Config-Based Loot
Use `falling-stars-loot` to define weighted single-item rewards:

```yaml
falling-stars-loot:
  nether_star: 0.05
  apple: 33
  blaze_spawn_egg: 33
  blue_orchid: 33
```

Material names must match Bukkit `Material` names valid for your server version.

#### Loot Tables
Use `falling-stars-loot-table` to hand reward generation to Minecraft:

```yaml
falling-stars-loot-table: "minecraft:chests/simple_dungeon"
```

Loot tables are better if you want multiple drops, more complex rewards, or NBT data.

### World Overrides
You can override global settings for specific worlds:

```yaml
falling-stars-spark-time: 200
falling-stars-experience: 25
falling-stars-loot:
  diamond: 1
  emerald: 0
  fire_charge: 0

world-overrides:
  some_world:
    falling-stars-enabled: false
  another_world:
    falling-stars-loot-table: "minecraft:chests/simple_dungeon"
    falling-stars-experience: 300
    adaptive-falling-stars: 1
    falling-stars-daylight: true
    falling-stars-day-multiplicative: 0.25
```

Any world not listed under `world-overrides` continues to use the global settings.

## Commands
`/shootingstar [player]` summons a shooting star near the target player, or near the sender if no player is supplied.  
Permission: `celeste.shootingstar`

`/fallingstar [player]` summons a falling star near the target player, or near the sender if no player is supplied.  
Permission: `celeste.fallingstar`

`/celeste reload` reloads the plugin configuration and rebuilds the active config set.  
Permission: `celeste.reload`

`/celeste info` shows whether shooting stars, falling stars, and meteor showers are enabled, plus the highest current falling star rate.  
Permission: `celeste.info`

## Credits
- Original plugin by [IdreesInc](https://github.com/IdreesInc)
- Fork build and platform migration work by [NotAlexNoyle](https://github.com/NotAlexNoyle)
- Falling star max height config by [SpaceXCheeseWheel](https://github.com/SpaceXCheeseWheel)
- Adaptive spawn scaling, daytime falling stars, and `/celeste info` rate display by [rm20killer](https://github.com/rm20killer)
