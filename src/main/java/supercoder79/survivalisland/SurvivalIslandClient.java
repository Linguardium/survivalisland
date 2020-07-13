package supercoder79.survivalisland;

import me.shedaniel.clothconfig2.api.AbstractConfigListEntry;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.impl.builders.DropdownMenuBuilder;
import mod.linguardium.wtflib.CreateWorldScreenSettings;
import mod.linguardium.wtflib.WorldTypeRegistry;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.gen.GeneratorOptions;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.ChunkGeneratorType;
import net.minecraft.world.gen.chunk.SurfaceChunkGenerator;
import sun.java2d.Surface;
import supercoder79.survivalisland.config.ConfigData;
import supercoder79.survivalisland.world.IslandBiomeSource;
import supercoder79.survivalisland.world.IslandWorldType;

import java.util.Optional;

@Environment(EnvType.CLIENT)
public class SurvivalIslandClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        WorldTypeRegistry.registerWorldType("island", IslandWorldType::new,(createWorldScreen, generatorOptions) -> {

            ChunkGenerator chunkGenerator = generatorOptions.getChunkGenerator();
            ConfigData config = (chunkGenerator.getBiomeSource() instanceof IslandBiomeSource)?((IslandBiomeSource) chunkGenerator.getBiomeSource()).getConfigData():new ConfigData();

            ConfigBuilder builder = ConfigBuilder.create().setParentScreen(createWorldScreen).setTitle(new TranslatableText("title.island.config"));
            ConfigCategory cat = builder.getOrCreateCategory(new TranslatableText("category.island.worldgen"));
            cat.addEntry(builder.entryBuilder().startStrField(new LiteralText("Starting Biome"),config.startingBiome).setErrorSupplier(v->{
                Biome id = Registry.BIOME.get(new Identifier(v));
                if(id != null && IslandBiomeSource.BIOMES.contains(id)) {
                    return Optional.empty();
                }else{
                    return Optional.of(new LiteralText("Invalid Biome"));
                }
            }).build());
/*cat.addEntry(builder.entryBuilder().startSelector(,IslandBiomeSource.BIOMES.stream().map(b-> {
                Identifier id = Registry.BIOME.getId(b);
                if (id != null)
                    return id.toString();
                else
                    return "zzzUnknownBiome";
            }
            ).toArray(String[]::new),config.startingBiome).setSaveConsumer(v->config.startingBiome=v).build());*/
            cat.addEntry(builder.entryBuilder().startIntField(new LiteralText("Island Size"),config.islandSize).setSaveConsumer(v->config.islandSize=v).build());
            cat.addEntry(builder.entryBuilder().startIntField(new LiteralText("Island Rarity"),config.islandRarity).setSaveConsumer(v->config.islandRarity=v).build());
            cat.addEntry(builder.entryBuilder().startBooleanToggle(new LiteralText("Separate Biomes"),config.seperateBiomes).setSaveConsumer(v->config.seperateBiomes=v).build());
            cat.addEntry(builder.entryBuilder().startBooleanToggle(new LiteralText("Generate Beaches"),config.generateBeaches).setSaveConsumer(v->config.generateBeaches=v).build());
            cat.addEntry(builder.entryBuilder().startBooleanToggle(new LiteralText("Hardcore Mode"),config.hardcoreMode).setSaveConsumer(v->config.hardcoreMode=v).build());
            builder.setSavingRunnable(
                    ()-> CreateWorldScreenSettings.setGeneratorOptions(createWorldScreen,
                            new GeneratorOptions(generatorOptions.getSeed(), generatorOptions.shouldGenerateStructures(), generatorOptions.hasBonusChest(), GeneratorOptions.method_28608(generatorOptions.getDimensionMap(), new SurfaceChunkGenerator(new IslandBiomeSource(generatorOptions.getSeed(),config),generatorOptions.getSeed(), ChunkGeneratorType.Preset.OVERWORLD.getChunkGeneratorType())))
                    )
            );
            return builder.build();
        });
    }
}
