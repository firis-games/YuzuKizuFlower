package firis.yuzukizuflower.common.world.generator;

import java.util.Random;

import com.google.common.base.Predicate;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraft.world.gen.feature.WorldGenerator;
import vazkii.botania.common.block.ModBlocks;

public class WorldGenMinableAlfheim extends WorldGenMinable {

	public WorldGenMinableAlfheim(IBlockState state, int blockCount) {
		super(state, blockCount, new AlfheimPredicate());
	}
	
	/**
	 * 鉱石を生成する
	 * @param world
	 * @param pos
	 * @param blockCount
	 * @param minHeight
	 * @param maxHeight
	 */
	public void generatorOre(World world, BlockPos pos, int blockCount, int minHeight, int maxHeight) {
		this.genStandardOre1(world, pos, world.rand, blockCount, this, minHeight, maxHeight);
	}
	
	/**
	 * net.minecraft.world.biome.BiomeDecorator genStandardOre1
	 * 標準処理と同じ処理を実装
	 * ChunkをBlockPosで動くように改変
	 * 
     * Standard ore generation helper. Vanilla uses this to generate most ores.
     * The main difference between this and {@link #genStandardOre2} is that this takes min and max heights, while
     * genStandardOre2 takes center and spread.
     */
    protected void genStandardOre1(World worldIn, BlockPos pos, Random random, int blockCount, WorldGenerator generator, int minHeight, int maxHeight)
    {
        if (maxHeight < minHeight)
        {
            int i = minHeight;
            minHeight = maxHeight;
            maxHeight = i;
        }
        else if (maxHeight == minHeight)
        {
            if (minHeight < 255)
            {
                ++maxHeight;
            }
            else
            {
                --minHeight;
            }
        }

        for (int j = 0; j < blockCount; ++j)
        {
            //BlockPos blockpos = this.chunkPos.add(random.nextInt(16), random.nextInt(maxHeight - minHeight) + minHeight, random.nextInt(16));
        	BlockPos blockpos = pos.add(random.nextInt(16), random.nextInt(maxHeight - minHeight) + minHeight, random.nextInt(16));
            generator.generate(worldIn, random, blockpos);
        }
    }
    
    static class AlfheimPredicate implements Predicate<IBlockState>
    {
        private AlfheimPredicate()
        {
        }

        public boolean apply(IBlockState p_apply_1_)
        {
            if (p_apply_1_ != null && p_apply_1_.getBlock() == ModBlocks.livingrock)
            {
            	/*
                BlockStone.EnumType blockstone$enumtype = (BlockStone.EnumType)p_apply_1_.getValue(BlockStone.VARIANT);
                return blockstone$enumtype.isNatural();
                */
            	return true;
            }
            else
            {
                return false;
            }
        }
    }
}
