package supercoder79.survivalisland;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.JsonOps;
import mod.linguardium.wtflib.WorldTypeRegistry;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.registry.RegistryEntryAddedCallback;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.SimpleRegistry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.GeneratorOptions;
import net.minecraft.world.gen.chunk.ChunkGeneratorType;
import net.minecraft.world.gen.chunk.FlatChunkGenerator;
import net.minecraft.world.gen.chunk.FlatChunkGeneratorConfig;
import net.minecraft.world.gen.chunk.SurfaceChunkGenerator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import supercoder79.survivalisland.config.ConfigData;
import supercoder79.survivalisland.world.IslandBiomeSource;

import java.util.stream.Collectors;

public class SurvivalIsland implements ModInitializer {

	@Override
	public void onInitialize() {
		Registry.register(Registry.BIOME_SOURCE, new Identifier("survivalisland", "island"), IslandBiomeSource.CODEC);
		Registry.BIOME.forEach((biome)-> {if (isValidBiome(biome)){IslandBiomeSource.BIOMES.add(biome);}});
		RegistryEntryAddedCallback.event(Registry.BIOME).register((val,id,biome)->{
			if (isValidBiome(biome)){IslandBiomeSource.BIOMES.add(biome);}
		});
		WorldTypeRegistry.registerLevelType("island",(seed,properties)->{
			Logger LOGGER = LogManager.getLogger();
			SimpleRegistry<DimensionOptions> dimensions = DimensionType.method_28517(seed);

			String generate_structures = (String)properties.get("generate-structures");
			String generatorSettings = (String)properties.get("generator-settings");
			boolean generateStructures = generate_structures == null || Boolean.parseBoolean(generate_structures);
			JsonObject jsonObject = !generatorSettings.isEmpty() ? JsonHelper.deserialize(generatorSettings) : new JsonObject();
			Dynamic<JsonElement> dynamic = new Dynamic<>(JsonOps.INSTANCE, jsonObject);
			DataResult<IslandBiomeSource> result = IslandBiomeSource.CODEC.parse(dynamic);
			return new GeneratorOptions(seed, generateStructures, false, GeneratorOptions.method_28608(dimensions, new SurfaceChunkGenerator(result.resultOrPartial(LOGGER::error).orElseGet(()->new IslandBiomeSource(seed, new ConfigData())),seed,  ChunkGeneratorType.Preset.OVERWORLD.getChunkGeneratorType())));
		});
	}
	public static boolean isValidBiome(Biome b) {
		Biome.Category bCat = b.getCategory();
		return !bCat.equals(Biome.Category.THEEND) && !bCat.equals(Biome.Category.NETHER) && !bCat.equals(Biome.Category.NONE) && !bCat.equals(Biome.Category.RIVER);
	}
}
