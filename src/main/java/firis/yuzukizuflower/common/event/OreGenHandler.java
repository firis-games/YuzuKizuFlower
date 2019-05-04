package firis.yuzukizuflower.common.event;

import firis.yuzukizuflower.common.world.dimension.DimensionHandler;
import firis.yuzukizuflower.common.world.generator.WorldGenMinableAlfheim;
import net.minecraft.init.Blocks;
import net.minecraftforge.event.terraingen.OreGenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class OreGenHandler {
	
	@SubscribeEvent
	public static void onOreGen(OreGenEvent event) {
		
		
		if (event.getWorld().provider.getDimension() !=
				DimensionHandler.dimensionAlfheim.getId()) {
			return;
		}
		
		//鉄鉱石
		WorldGenMinableAlfheim gen = new WorldGenMinableAlfheim(
				Blocks.IRON_ORE.getDefaultState(), 9);

		//鉱石生成
		gen.generatorOre(event.getWorld(), event.getPos(), 20, 1, 64);
		
	}
	
}
