package firis.yuzukizuflower.common.event;

import firis.yuzukizuflower.YuzuKizuFlower.YuzuKizuBlocks;
import firis.yuzukizuflower.common.world.dimension.DimensionHandler;
import firis.yuzukizuflower.common.world.generator.WorldGenMinableAlfheim;
import net.minecraft.init.Blocks;
import net.minecraftforge.event.terraingen.OreGenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class OreGenHandler {
	
	//土
	public static WorldGenMinableAlfheim dirtGen = new WorldGenMinableAlfheim(
			Blocks.DIRT.getDefaultState(), 33, 12, 11, 256);
	
	//砂利
	public static WorldGenMinableAlfheim gravelGen = new WorldGenMinableAlfheim(
			Blocks.GRAVEL.getDefaultState(), 30, 3, 1, 256);
	
	//砂（独自）
	public static WorldGenMinableAlfheim sandGen = new WorldGenMinableAlfheim(
			Blocks.SAND.getDefaultState(), 30, 3, 1, 80);

	//粘土（独自）
	public static WorldGenMinableAlfheim clayGen = new WorldGenMinableAlfheim(
			Blocks.CLAY.getDefaultState(), 30, 3, 1, 80);
	
	//鉄鉱石
	public static WorldGenMinableAlfheim ironGen = new WorldGenMinableAlfheim(
			Blocks.IRON_ORE.getDefaultState(), 9, 20, 1, 64);

	//金鉱石
	public static WorldGenMinableAlfheim goldGen = new WorldGenMinableAlfheim(
			Blocks.GOLD_ORE.getDefaultState(), 9, 2, 1, 32);
	
	//レッドストーン鉱石
	public static WorldGenMinableAlfheim redStoneGen = new WorldGenMinableAlfheim(
			Blocks.GOLD_ORE.getDefaultState(), 8, 8, 1, 16);

	//ダイヤモンド鉱石
	public static WorldGenMinableAlfheim diamondGen = new WorldGenMinableAlfheim(
			Blocks.DIAMOND_ORE.getDefaultState(), 8, 1, 1, 16);
	
	//ピクシィ鉱石（鉄鉱石と同じ）
	public static WorldGenMinableAlfheim pixieGen = new WorldGenMinableAlfheim(
			YuzuKizuBlocks.PIXIE_ORE.getDefaultState(), 9, 20, 1, 64);
	
	//エレブン鉱石（石炭と同じ）
	public static WorldGenMinableAlfheim elevnGen = new WorldGenMinableAlfheim(
			YuzuKizuBlocks.ELEVN_ORE.getDefaultState(), 17, 20, 1, 128);
	
	//エレメンチム鉱石（鉄鉱石と同じ）
	public static WorldGenMinableAlfheim elementiumGen = new WorldGenMinableAlfheim(
			YuzuKizuBlocks.ELEMENTIUM_ORE.getDefaultState(), 9, 20, 1, 64);

	//ドラゴン鉱石（金鉱石と同じ）
	public static WorldGenMinableAlfheim dragonGen = new WorldGenMinableAlfheim(
			YuzuKizuBlocks.DRAGON_ORE.getDefaultState(), 9, 2, 1, 32);

	//ガイア鉱石（ダイヤ鉱石と同じ）
	public static WorldGenMinableAlfheim gaiaGen = new WorldGenMinableAlfheim(
			YuzuKizuBlocks.GAIA_ORE.getDefaultState(), 8, 1, 1, 16);
	
	@SubscribeEvent
	public static void onOreGen(OreGenEvent.Post event) {
		
		if (event.getWorld().provider.getDimension() !=
				DimensionHandler.dimensionAlfheim.getId()) {
			return;
		}
		
		//鉱石生成
		dirtGen.generatorOre(event.getWorld(), event.getPos());
		gravelGen.generatorOre(event.getWorld(), event.getPos());
		sandGen.generatorOre(event.getWorld(), event.getPos());
		clayGen.generatorOre(event.getWorld(), event.getPos());
		ironGen.generatorOre(event.getWorld(), event.getPos());
		goldGen.generatorOre(event.getWorld(), event.getPos());
		redStoneGen.generatorOre(event.getWorld(), event.getPos());
		diamondGen.generatorOre(event.getWorld(), event.getPos());
		
		pixieGen.generatorOre(event.getWorld(), event.getPos());
		elevnGen.generatorOre(event.getWorld(), event.getPos());
		elementiumGen.generatorOre(event.getWorld(), event.getPos());
		dragonGen.generatorOre(event.getWorld(), event.getPos());
		gaiaGen.generatorOre(event.getWorld(), event.getPos());
		
	}
	
}
