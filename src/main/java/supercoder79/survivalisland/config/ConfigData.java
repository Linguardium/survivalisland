package supercoder79.survivalisland.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import supercoder79.survivalisland.world.IslandBiomeSource;

public class ConfigData {
    public static final Codec<ConfigData> CODEC = RecordCodecBuilder
            .create((instance) -> instance.group(
                    Codec.INT.fieldOf("size").forGetter((a)->a.islandSize),
                    Codec.INT.fieldOf("rarity").forGetter((a)->a.islandRarity),
                    Codec.BOOL.fieldOf("beaches").forGetter(a->a.generateBeaches),
                    Codec.BOOL.fieldOf("biomes").forGetter(a->a.seperateBiomes),
                    Codec.BOOL.fieldOf("hardcore").forGetter(a->a.hardcoreMode),
                    Codec.STRING.fieldOf("starting").forGetter((a)->a.startingBiome)
            )
                    .apply(instance, instance.stable(ConfigData::new)));

    public int islandSize = 5;
    public int islandRarity = 6;

    public String startingBiome = "minecraft:forest";
    public boolean generateBeaches = true;
    public boolean seperateBiomes = true;

    public boolean hardcoreMode = false;

    public ConfigData() {}
    public ConfigData(int size, int rarity, boolean beaches, boolean biomes, boolean hardcore, String starting) {
        this.islandSize=size;
        this.islandRarity=rarity;
        this.generateBeaches=beaches;
        this.seperateBiomes=biomes;
        this.hardcoreMode=hardcore;
        this.startingBiome=starting;
    }
}
