package firis.yuzukizuflower.common.dimension;

import net.minecraft.init.Biomes;
import net.minecraft.world.DimensionType;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.biome.BiomeProviderSingle;
import net.minecraft.world.gen.ChunkGeneratorOverworld;
import net.minecraft.world.gen.IChunkGenerator;

/**
 * ディメンションのprovider
 * @author computer
 *
 */
public class WorldProviderAlfheim extends WorldProvider {

	@Override
	public DimensionType getDimensionType() {
		return DimensionHandler.dimensionAlfheim;
	}

	@Override
    protected void init() {
        
		this.doesWaterVaporize = false;
		this.hasSkyLight = true;

		//平原
		this.biomeProvider = new BiomeProviderSingle(Biomes.PLAINS);
    }
	
	@Override
    public IChunkGenerator createChunkGenerator()
    {
		return new ChunkGeneratorOverworld(world, world.getSeed(), world.getWorldInfo().isMapFeaturesEnabled(), "");
    }
}
