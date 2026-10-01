# NoGrief — Fabric 1.21.1

A lightweight client-side mod designed to prevent accidental griefing during PvP and PvE combat.

## Features

When protection is enabled:

- Left-clicking blocks is blocked, so missed attacks do not start breaking the terrain or nearby structures.
- Attacking players and mobs remains unaffected.
- Placing ordinary blocks and using buckets, flint and steel, and fire charges on blocks is blocked.
- Placing and detonating End Crystals remains possible.
- Normal interactions with doors, chests, buttons, etc. remain available when you are not holding one of the restricted items.

The status message above the hotbar is light green when protection is enabled and light red when disabled.

Protection is disabled by default. Press **F12** to toggle it, or rebind the key in Minecraft's Controls settings under **NoGrief**.

## Requirements

- Minecraft 1.21.1
- Fabric Loader 0.16.14+
- Fabric API
- Java 21
- Gradle 8.14.3 to build the project

## Build

From the project root:

```bash
./gradlew build
```

The mod JAR will be generated at:

```text
build/libs/NoGrief-fabric-1.0.1.jar
```

Do not place the `*-sources.jar` in your Minecraft `mods` folder; it contains source code, not the playable mod.
