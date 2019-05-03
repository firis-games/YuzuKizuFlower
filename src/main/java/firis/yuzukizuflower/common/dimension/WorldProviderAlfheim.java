package firis.yuzukizuflower.common.dimension;

import firis.yuzukizuflower.YuzuKizuFlower;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.DimensionType;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeProviderSingle;
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

		//マナ平原
		Biome biome = Biome.REGISTRY.getObject(new ResourceLocation(YuzuKizuFlower.MODID, "mana_plains"));
		this.biomeProvider = new BiomeProviderSingle(biome);
    }
	
	@Override
    public IChunkGenerator createChunkGenerator()
    {
		return new ChunkGeneratorAlfheim(world, world.getSeed(), world.getWorldInfo().isMapFeaturesEnabled(), "");
    }
}
