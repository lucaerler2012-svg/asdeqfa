# Reduced Effects

Ein Fabric-Mod für Minecraft 26.3, der visuelle Effekte reduziert:

## Features

### 🔥 Feuer-Effekte reduziert
- **Langsamere Animation**: Feuer-Partikel bewegen sich deutlich langsamer
- **Niedrigere Flammen**: Flammenhöhe um ca. 50% reduziert (von 15 auf 8)
- **Geringere Ausbreitung**: Feuer breitet sich nur halb so schnell aus

### 💥 Explosionen entfernt
- **TNT**: Keine Explosions-Partikel, Blöcke verschwinden direkt
- **End Crystals**: Keine Explosions-Animation beim Zerstören
- **Respawn Anchor**: Keine Explosion im Overworld/End beim Aufladen mit Glowstone

## Installation

1. Lade die neueste `.jar` aus den [Releases](https://github.com/lucae/reduced-effects/releases) herunter
2. Lege die Datei in deinen `mods` Ordner
3. Starte Minecraft mit Fabric Loader 0.20.1+ für Minecraft 26.3

## Kompatibilität

- **Minecraft**: 26.3
- **Fabric Loader**: 0.20.1+
- **Fabric API**: 0.165.2+
- **Java**: 25

## Bauen

```bash
./gradlew build
```

Die fertige JAR liegt in `build/libs/`.

## Lizenz

MIT License - siehe [LICENSE](LICENSE)