package firis.yuzukizuflower.common.world.dimension;

import firis.yuzukizuflower.YuzuKizuFlower;
import firis.yuzukizuflower.YuzuKizuFlower.YuzuKizuBlocks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DimensionType;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeProviderSingle;
import net.minecraft.world.chunk.Chunk;
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
	
	/**
	 * スポーン位置を設定する
	 */
	public BlockPos getRandomizedSpawnPoint()
    {
		//ゲートの位置へスポーンさせる
		BlockPos pos = new BlockPos(0, 0, 0);
		Chunk chunk = this.world.getChunkFromBlockCoords(pos);
		pos = new BlockPos(pos.getX(), chunk.getTopFilledSegment() + 16, pos.getZ());
		
		//アルフヘイムコアを検索
		while (pos.getY() >= 0) {
			IBlockState state = chunk.getBlockState(pos);
			
			if (state.getBlock() == YuzuKizuBlocks.ALFHEIM_CORE) {
				//位置調整
				pos = pos.down(5);
				break;
			}
			pos = pos.down();
		}
		
		//取得できない場合
		if (pos.getY() <= 0) {
			pos = this.world.getTopSolidOrLiquidBlock(new BlockPos(0, 0, 0)).up(3);
		}
		
        return pos;
    }
}
