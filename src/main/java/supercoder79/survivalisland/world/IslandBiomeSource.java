package supercoder79.survivalisland.world;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.biome.source.BiomeLayerSampler;
import net.minecraft.world.biome.source.BiomeSource;
import supercoder79.survivalisland.config.ConfigData;

import java.util.List;

public class IslandBiomeSource extends BiomeSource {
    public static final Codec<IslandBiomeSource> CODEC = Codec.LONG.fieldOf("seed").xmap(IslandBiomeSource::new, (source) -> source.seed).stable().codec();
    private final BiomeLayerSampler biomeSampler;
    private final long seed;
    public static List<Biome> BIOMES = Lists.newArrayList();
    private int islandSize = 5;
    private int islandRarity = 6;
    private boolean generateBeaches = true;
    private boolean uniqueBiomes = true;
    private boolean hardcoreMode = false;
    private String startingBiome = "minecraft:forest";
    @Deprecated
    public IslandBiomeSource(long seed) {
        this(seed,new ConfigData());
    }
    public IslandBiomeSource(long seed, ConfigData data) {
        super(BIOMES);
        this.seed = seed;
        this.islandRarity=data.islandRarity;
        this.islandSize=data.islandSize;
        this.generateBeaches=data.generateBeaches;
        this.uniqueBiomes=data.seperateBiomes;
        this.hardcoreMode=data.hardcoreMode;
        this.startingBiome=data.startingBiome;
        this.biomeSampler = IslandBiomeLayers.build(seed, getConfigData());
    }
    public ConfigData getConfigData() {
        ConfigData d = new ConfigData();
        d.generateBeaches=generateBeaches;
        d.hardcoreMode=hardcoreMode;
        d.seperateBiomes=uniqueBiomes;
        d.islandSize=islandSize;
        d.islandRarity=islandRarity;
        d.startingBiome=startingBiome;
        return d;
    }
    public Biome getBiomeForNoiseGen(int biomeX, int biomeY, int biomeZ) {
        return this.biomeSampler.sample(biomeX, biomeZ);
    }

    @Override
    protected Codec<? extends BiomeSource> method_28442() {
        return CODEC;
    }

    @Override
    public BiomeSource withSeed(long seed) {
        return new IslandBiomeSource(seed,getConfigData());
    }
}
