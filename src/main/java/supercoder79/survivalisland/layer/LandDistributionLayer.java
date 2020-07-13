package supercoder79.survivalisland.layer;

import net.minecraft.world.biome.source.BiomeSource;
import supercoder79.survivalisland.SurvivalIsland;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.biome.layer.type.InitLayer;
import net.minecraft.world.biome.layer.util.LayerFactory;
import net.minecraft.world.biome.layer.util.LayerRandomnessSource;
import net.minecraft.world.biome.layer.util.LayerSampleContext;
import net.minecraft.world.biome.layer.util.LayerSampler;
import supercoder79.survivalisland.config.ConfigData;
import supercoder79.survivalisland.world.IslandBiomeSource;

public enum LandDistributionLayer implements InitLayer {
    INSTANCE;
    ConfigData data;
    public <R extends LayerSampler> LayerFactory<R> create(ConfigData d, LayerSampleContext<R> context) {
        /*biomes = Registry.BIOME.stream()
                .filter(p -> p.getCategory() != Biome.Category.NETHER && p.getCategory() != Biome.Category.THEEND && p.getCategory() != Biome.Category.RIVER && p.getCategory() != Biome.Category.NONE)
                .toArray(Biome[]::new);
        */
        this.data = d;
        return () -> context.createSampler((x, z) -> {
            context.initSeed(x, z);
            return this.sample(context, x, z);
        });
    }

    @Override
    public int sample(LayerRandomnessSource context, int x, int z) {

        if (x == 0 && z == 0) {
            Biome b = Registry.BIOME.get(new Identifier(data.startingBiome));
            if (b==null) {
                return Registry.BIOME.getRawId(Biomes.PLAINS);
            }else{
                Registry.BIOME.getRawId(b);
            }
        }
        if (IslandBiomeSource.BIOMES.size()>0 && context.nextInt(data.islandRarity)==0)
            return Registry.BIOME.getRawId(IslandBiomeSource.BIOMES.get(context.nextInt(IslandBiomeSource.BIOMES.size())));
        return 0;
    }
}
