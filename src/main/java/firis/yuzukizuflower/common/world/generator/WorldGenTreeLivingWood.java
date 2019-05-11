package firis.yuzukizuflower.common.world.generator;

import firis.yuzukizuflower.YuzuKizuFlower.YuzuKizuBlocks;
import net.minecraft.block.BlockLeaves;
import net.minecraft.world.gen.feature.WorldGenTrees;
import vazkii.botania.common.block.ModBlocks;

public class WorldGenTreeLivingWood extends WorldGenTrees {
	
	/**
	 * リビングウッドの木を生成する
	 * @param notify
	 */
	public WorldGenTreeLivingWood(boolean notify) {
		super(notify,
				4,
				ModBlocks.dreamwood.getDefaultState(),
				YuzuKizuBlocks.DREAM_LEAF.getDefaultState()
					.withProperty(BlockLeaves.CHECK_DECAY,Boolean.valueOf(false)),
				false);
	}

}
