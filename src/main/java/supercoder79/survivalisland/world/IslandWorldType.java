package supercoder79.survivalisland.world;

import mod.linguardium.wtflib.CustomGeneratorType;
import net.minecraft.client.world.GeneratorType;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.ChunkGeneratorType;
import net.minecraft.world.gen.chunk.SurfaceChunkGenerator;

public class IslandWorldType extends CustomGeneratorType {
    public IslandWorldType(String type) {
        super(type);
    }

    @Override
    protected ChunkGenerator method_29076(long l) {
        return new SurfaceChunkGenerator(new IslandBiomeSource(l), l, ChunkGeneratorType.Preset.OVERWORLD.getChunkGeneratorType());
    }
}
