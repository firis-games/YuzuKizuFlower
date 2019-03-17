package firis.yuzukizuflower.common.event;

import java.util.Arrays;
import java.util.List;

import firis.yuzukizuflower.YuzuKizuFlower.YuzuKizuBlocks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.feature.WorldGenLakes;
import net.minecraftforge.event.terraingen.PopulateChunkEvent;
import net.minecraftforge.event.terraingen.PopulateChunkEvent.Populate;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * net.minecraft.world.gen.ChunkGeneratorOverworld
 * 
 * 池や溶岩湖を生成する際に呼ばれるイベント
 * PopulateChunkEvent.Populate
 * 
 * 独自でやりたい場合は
 * PopulateChunkEvent.Pre or PopulateChunkEvent.Post
 * 
 * 花やかぼちゃなどを設置したい場合は
 * DecorateBiomeEvent.Decorate
 *  
 */
@EventBusSubscriber
public class PopulateChunkEventHandler {

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void populateChunkEvent(PopulateChunkEvent.Populate event) {
		
		//EventType
		if((event.getResult() == Result.ALLOW || event.getResult() == Result.DEFAULT) 
				&& event.getType() == Populate.EventType.LAKE) {
			
			//20%で池生成を差し替える
			if (event.getRand().nextInt(100) > 20) return;
			
			BlockPos chunkPos = new BlockPos(event.getChunkX(), 0, event.getChunkZ());
			
			int i1 = event.getRand().nextInt(16) + 8;
            int j1 = event.getRand().nextInt(256);
            int k1 = event.getRand().nextInt(16) + 8;
            
            (new WorldGenLakes(YuzuKizuBlocks.LIQUID_MANA)).generate(event.getWorld(), event.getRand(), chunkPos.add(i1, j1, k1));
			
			event.setResult(Result.DENY);
		}
	}
	
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void decorateBiomeEvent(PopulateChunkEvent.Pre event) {
		
		if((event.getResult() == Result.ALLOW || event.getResult() == Result.DEFAULT)) {
			
			//1%で1ブロックのマナたまりを生成
			if (event.getRand().nextInt(100) > 1) return;
			
			//1-14の間の座標をランダムで取得する
			Chunk chunk = event.getWorld().getChunkFromChunkCoords(event.getChunkX(), event.getChunkZ());
			int x = chunk.getPos().getXStart() + event.getRand().nextInt(14) + 1;
			int z = chunk.getPos().getZStart() + event.getRand().nextInt(14) + 1;
			
			//base
			BlockPos basePos = new BlockPos(x, 0, z);
			BlockPos pos = event.getWorld().getTopSolidOrLiquidBlock(basePos).down();
			
			//置き換えるブロックの4方向と下を確認する
			List<EnumFacing> facingList = Arrays.asList(EnumFacing.NORTH, EnumFacing.WEST, EnumFacing.SOUTH, EnumFacing.EAST, EnumFacing.DOWN);
			boolean ret = true;
			for (EnumFacing facing : facingList) {
				BlockPos facingPos = pos.offset(facing);
				IBlockState facingState = event.getWorld().getBlockState(facingPos);
				if(!facingState.getMaterial().isSolid()) {
					ret = false;
					break;
				}
			}
			if (ret) {
				//上のブロックを置換
				for (int i = 1; i <= 3; i++) {
					BlockPos upPos = pos.up(i);
					IBlockState upState = event.getWorld().getBlockState(upPos);
					if(!upState.getMaterial().isSolid()) {
						event.getWorld().setBlockState(upPos, Blocks.AIR.getDefaultState(), 2);
					}
				}
				event.getWorld().setBlockState(pos, YuzuKizuBlocks.LIQUID_MANA.getDefaultState(), 2);
			}			
		}
		
	}
}
