package firis.yuzukizuflower.common.event;

import java.util.Arrays;
import java.util.List;

import firis.yuzukizuflower.YuzuKizuFlower.YuzuKizuBlocks;
import firis.yuzukizuflower.common.YKConfig;
import firis.yuzukizuflower.common.world.dimension.DimensionHandler;
import firis.yuzukizuflower.common.world.generator.WorldGenAlfheimPortal;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.WorldType;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.feature.WorldGenLakes;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.event.terraingen.PopulateChunkEvent;
import net.minecraftforge.event.terraingen.PopulateChunkEvent.Populate;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.api.state.enums.AltGrassVariant;
import vazkii.botania.common.block.ModBlocks;

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

	/**
	 * 池生成を一定確率でマナ池生成へ差し替える
	 * @param event
	 */
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void populateChunkEvent(PopulateChunkEvent.Populate event) {
		
		//EventType
		if((event.getResult() == Result.ALLOW || event.getResult() == Result.DEFAULT) 
				&& event.getType() == Populate.EventType.LAKE) {
			
			//池生成を差し替える
			//Configで生成率を制御
			if (YKConfig.GEN_RATE_MANA_LAKE == 0 ||
					event.getRand().nextInt(1000) > YKConfig.GEN_RATE_MANA_LAKE) return;
			
			ChunkPos chunkPos = new ChunkPos(event.getChunkX(), event.getChunkZ());
			chunkPos.getBlock(0, 0, 0);
			BlockPos blockPos = chunkPos.getBlock(0, 0, 0);
			
			int i1 = event.getRand().nextInt(16) + 8;
            int j1 = event.getRand().nextInt(256);
            int k1 = event.getRand().nextInt(16) + 8;
            
            (new WorldGenLakes(YuzuKizuBlocks.LIQUID_MANA)).generate(event.getWorld(), event.getRand(), blockPos.add(i1, j1, k1));
			
			event.setResult(Result.DENY);
		}
	}
	
	
	/**
	 * マナ溜りを生成
	 * @param event
	 */
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void decorateBiomeEvent(PopulateChunkEvent.Pre event) {
				
		//ワールドタイプFLAT・オーバーワールド以外は生成を行わない
		if (event.getWorld().getWorldType() == WorldType.FLAT
				|| !(event.getWorld().provider.getDimension() == 0)) {
			return;
		}
		
		if((event.getResult() == Result.ALLOW || event.getResult() == Result.DEFAULT)) {
			
			//マナ溜りの生成
			//Configで生成率を制御
			if (YKConfig.GEN_RATE_MANA_POOL == 0 ||
					event.getRand().nextInt(1000) > YKConfig.GEN_RATE_MANA_POOL) return;
			
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
			
			//上のブロックが液体の場合は処理しない
			if (ret) {
				BlockPos facingPos = pos.offset(EnumFacing.UP);
				IBlockState facingState = event.getWorld().getBlockState(facingPos);
				if(facingState.getMaterial().isLiquid()) {
					ret = false;
				}
			}
			
			//固体ブロック確認
			if (ret) {
				for (EnumFacing facing : facingList) {
					BlockPos facingPos = pos.offset(facing);
					IBlockState facingState = event.getWorld().getBlockState(facingPos);
					//固体ブロックでない or 透過ブロックである
					if(!facingState.getMaterial().isSolid()
							|| !facingState.getMaterial().isOpaque()) {
						ret = false;
						break;
					}
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
	
	
	/**
	 * アルフヘイムの基点構造物を生成する
	 * @param event
	 */
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void onPopulateChunkEventPost(PopulateChunkEvent.Post event) {
		
		//アルフヘイム以外は処理を行わない
		if (event.getWorld().provider.getDimension() != DimensionHandler.dimensionAlfheim.getId()) {
			return;
		}
		if (event.getResult() != Result.ALLOW && event.getResult() != Result.DEFAULT) {
			return;
		}
		
		//座標を確認
		Chunk chunk = event.getWorld().getChunkFromChunkCoords(event.getChunkX(), event.getChunkZ());
		//x座標z座標が基点
		if (chunk.x == 0 && chunk.z == 0) {
			
			//基準点
			BlockPos basePos = event.getWorld().getTopSolidOrLiquidBlock(new BlockPos(0, 0, 0)).down();
			
			//上のブロックが液体の場合は処理しない(高さ128まで計算)
			basePos = basePos.up();
			while (basePos.getY() < 128) {
				IBlockState state = event.getWorld().getBlockState(basePos);
				if(!state.getMaterial().isLiquid()) {
					break;
				}
				basePos = basePos.up();
			}
			basePos = basePos.down();
			
			//一定空間を空にする
			int range = 13;
			for (BlockPos pos : BlockPos.getAllInBox(
					basePos.north(range).west(range).up(range),
					basePos.south(range).east(range).up())) {
				event.getWorld().setBlockState(pos, Blocks.AIR.getDefaultState(), 2);
			}
			
			IBlockState baseState;
			
			//土を変更
			baseState = ModBlocks.altGrass.getDefaultState().withProperty(BotaniaStateProps.ALTGRASS_VARIANT, AltGrassVariant.INFUSED);
			for (BlockPos pos : BlockPos.getAllInBox(
					basePos.north(range).west(range),
					basePos.south(range).east(range))) {
				if (event.getWorld().getBlockState(pos).getBlock() == Blocks.DIRT) {
					event.getWorld().setBlockState(pos, Blocks.AIR.getDefaultState(), 2);
				}
			}
			
			//一定空間に土台をつくる
			range = 8;
			baseState = ModBlocks.enchantedSoil.getDefaultState();
			for (BlockPos pos : BlockPos.getAllInBox(
					basePos.north(range).west(range),
					basePos.south(range).east(range))) {
				event.getWorld().setBlockState(pos, baseState, 2);
			}
			//土
			baseState = Blocks.DIRT.getDefaultState();
			for (BlockPos pos : BlockPos.getAllInBox(
					basePos.north(range).west(range).down(1),
					basePos.south(range).east(range).down(4))) {
				event.getWorld().setBlockState(pos, baseState, 2);
			}
			//リビングロック
			baseState = ModBlocks.livingrock.getDefaultState();
			for (BlockPos pos : BlockPos.getAllInBox(
					basePos.north(range).west(range).down(5),
					basePos.south(range).east(range).down(10))) {
				event.getWorld().setBlockState(pos, baseState, 2);
			}

			//アルフヘイムポータル
			WorldGenerator alfheimPortal = new WorldGenAlfheimPortal();
			alfheimPortal.generate(event.getWorld(), event.getWorld().rand, basePos);			
		}
	}
}
