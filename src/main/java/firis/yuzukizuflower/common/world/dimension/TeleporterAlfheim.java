package firis.yuzukizuflower.common.world.dimension;

import firis.yuzukizuflower.YuzuKizuFlower.YuzuKizuBlocks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;

public class TeleporterAlfheim extends Teleporter {

	public TeleporterAlfheim(WorldServer worldIn) {
		super(worldIn);
	}

	/**
	 * エンドと同じで黒曜石の足場を生成する
	 */
	public void placeInPortal(Entity entityIn, float rotationYaw)
    {
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
		
		//Playerの位置調整
        entityIn.setLocationAndAngles((double)pos.getX() + 0.5, 
        		(double)pos.getY(), 
        		(double)pos.getZ() + 0.5, entityIn.rotationYaw, 0.0F);
        entityIn.motionX = 0.0D;
        entityIn.motionY = 0.0D;
        entityIn.motionZ = 0.0D;
    }
}
